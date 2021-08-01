package com.dejavu.nunu.system.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dejavu.nunu.system.payment.entity.PaymentEntity;
import com.dejavu.nunu.system.payment.model.PaymentBaseBo;

public interface PaymentService extends IService<PaymentEntity> {


    /**
     * 创建订单
     * （需幂等）
     * @param paymentBaseBo
     */
    void create(PaymentBaseBo paymentBaseBo);
}
