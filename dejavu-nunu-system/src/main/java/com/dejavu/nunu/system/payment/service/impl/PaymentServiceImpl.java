package com.dejavu.nunu.system.payment.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dejavu.nunu.core.exception.BaseException;
import com.dejavu.nunu.core.utils.SecurityUtil;
import com.dejavu.nunu.system.global.exception.GlobalLockRepException;
import com.dejavu.nunu.system.global.service.GlobalLockService;
import com.dejavu.nunu.system.notice.service.NoticeService;
import com.dejavu.nunu.system.payment.constant.PaymentConstant;
import com.dejavu.nunu.system.payment.constant.PaymentStatusEnum;
import com.dejavu.nunu.system.payment.entity.PaymentEntity;
import com.dejavu.nunu.system.payment.exception.PaymentException;
import com.dejavu.nunu.system.payment.mapper.PaymentMapper;
import com.dejavu.nunu.system.payment.model.PaymentBaseBo;
import com.dejavu.nunu.system.payment.model.PaymentConfirmBo;
import com.dejavu.nunu.system.payment.model.PaymentCreateBo;
import com.dejavu.nunu.system.payment.service.PaymentService;
import com.dejavu.nunu.system.tenant.entity.TenantEntity;
import com.dejavu.nunu.system.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, PaymentEntity> implements PaymentService {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private GlobalLockService globalLockService;

    @Autowired
    private NoticeService noticeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PaymentBaseBo paymentBaseBo) {
        TenantEntity tenantEntity = tenantService.getByTenantId(paymentBaseBo.getTenantId());
        if (null == tenantEntity) {
            throw new BaseException(PaymentException.TENANT_ERROR);
        }
        //??????
        String apiKey = tenantEntity.getApiKey();
        String jsonContext = SecurityUtil.decrypt(apiKey, paymentBaseBo.getContext());
        PaymentCreateBo paymentCreateBo = JSONUtil.toBean(jsonContext, PaymentCreateBo.class);
        try {
            //?????????????????????
            this.create(tenantEntity.getId(), paymentCreateBo);
        } catch (GlobalLockRepException e) {
            //TODO ????????????
            log.error("???????????????" + e.getMessage());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(PaymentBaseBo paymentBaseBo) {
        TenantEntity tenantEntity = tenantService.getByTenantId(paymentBaseBo.getTenantId());
        if (null == tenantEntity) {
            throw new BaseException(PaymentException.TENANT_ERROR);
        }
        //??????
        String apiKey = tenantEntity.getApiKey();
        String jsonContext = SecurityUtil.decrypt(apiKey, paymentBaseBo.getContext());
        PaymentConfirmBo paymentConfirmBo = JSONUtil.toBean(jsonContext, PaymentConfirmBo.class);
        String orderNo = paymentConfirmBo.getOrderNo();
        //???????????????????????????
        noticeService.send(orderNo);
    }

    @Override
    public PaymentEntity getByOrderNo(String orderNo) {
        QueryWrapper<PaymentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PaymentEntity::getOrderNo, orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateStatus(Long tenantId, String orderNo, PaymentStatusEnum status) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setStatus(status);
        paymentEntity.setUpdatedTime(new Date());
        UpdateWrapper<PaymentEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PaymentEntity::getTenantId, tenantId);
        updateWrapper.lambda().eq(PaymentEntity::getOrderNo, orderNo);
        baseMapper.update(paymentEntity, updateWrapper);
    }


    /**
     * ?????????????????????
     * ???????????????????????????????????? GlobalLockRepException ?????????
     *
     * @param tenantId
     * @param paymentCreateBo
     * @throws GlobalLockRepException
     */
    private void create(Long tenantId, PaymentCreateBo paymentCreateBo) throws GlobalLockRepException {
        String orderNo = paymentCreateBo.getOrderNo();
        globalLockService.lock(tenantId, orderNo, PaymentConstant.Type.CREATE);

        //??????????????????
        Date now = new Date();
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setTenantId(tenantId);
        paymentEntity.setOrderNo(orderNo);
        paymentEntity.setAmount(paymentCreateBo.getAmount());
        paymentEntity.setCreatedTime(now);
        paymentEntity.setUpdatedTime(now);
        baseMapper.insert(paymentEntity);

    }


}
