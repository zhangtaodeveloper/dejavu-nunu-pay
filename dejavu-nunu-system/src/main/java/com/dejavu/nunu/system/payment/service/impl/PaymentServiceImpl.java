package com.dejavu.nunu.system.payment.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dejavu.nunu.core.exception.BaseException;
import com.dejavu.nunu.core.utils.SecurityUtil;
import com.dejavu.nunu.system.global.exception.GlobalLockRepException;
import com.dejavu.nunu.system.global.service.GlobalLockService;
import com.dejavu.nunu.system.notice.service.NoticeService;
import com.dejavu.nunu.system.payment.constant.PaymentConstant;
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
        //解密
        String apiKey = tenantEntity.getApiKey();
        String jsonContext = SecurityUtil.decrypt(apiKey, paymentBaseBo.getContext());
        PaymentCreateBo paymentCreateBo = JSONUtil.toBean(jsonContext, PaymentCreateBo.class);
        try {
            //创建待支付信息
            this.create(tenantEntity.getId(), paymentCreateBo);
        } catch (GlobalLockRepException e) {
            //TODO 重复请求
            log.error("重复请求：" + e.getMessage());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(PaymentBaseBo paymentBaseBo) {
        TenantEntity tenantEntity = tenantService.getByTenantId(paymentBaseBo.getTenantId());
        if (null == tenantEntity) {
            throw new BaseException(PaymentException.TENANT_ERROR);
        }
        //解密
        String apiKey = tenantEntity.getApiKey();
        String jsonContext = SecurityUtil.decrypt(apiKey, paymentBaseBo.getContext());
        PaymentConfirmBo paymentConfirmBo = JSONUtil.toBean(jsonContext, PaymentConfirmBo.class);
        String orderNo = paymentConfirmBo.getOrderNo();
        //进行通知收款人确认
        noticeService.send(orderNo);
    }

    @Override
    public PaymentEntity getByOrderNo(String orderNo) {
        QueryWrapper<PaymentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PaymentEntity::getOrderNo, orderNo);
        return baseMapper.selectOne(queryWrapper);
    }


    /**
     * 创建待支付记录
     * （幂等：当重复请求时抛出 GlobalLockRepException 异常）
     *
     * @param tenantId
     * @param paymentCreateBo
     * @throws GlobalLockRepException
     */
    private void create(Long tenantId, PaymentCreateBo paymentCreateBo) throws GlobalLockRepException {
        String orderNo = paymentCreateBo.getOrderNo();
        globalLockService.lock(tenantId, orderNo, PaymentConstant.Type.CREATE);

        //创建订单信息
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
