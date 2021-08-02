package com.dejavu.nunu.system.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dejavu.nunu.system.payment.entity.PaymentEntity;
import com.dejavu.nunu.system.payment.model.PaymentBaseBo;

public interface PaymentService extends IService<PaymentEntity> {


    /**
     * 创建支付信息
     * （幂等）
     *
     * @param paymentBaseBo
     */
    void create(PaymentBaseBo paymentBaseBo);

    /**
     * 确认支付信息
     * （幂等）
     *
     * @param paymentBaseBo
     */
    void confirm(PaymentBaseBo paymentBaseBo);

    /**
     * 查询支付记录
     *
     * @param orderNo 根据订单编号查询
     * @return
     */
    PaymentEntity getByOrderNo(String orderNo);
}
