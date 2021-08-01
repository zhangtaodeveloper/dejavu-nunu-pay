package com.dejavu.nunu.system;

import com.dejavu.nunu.core.utils.SecurityUtil;
import com.dejavu.nunu.system.payment.entity.PaymentEntity;
import com.dejavu.nunu.system.payment.model.PaymentBaseBo;
import com.dejavu.nunu.system.payment.model.PaymentCreateBo;
import com.dejavu.nunu.system.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Slf4j
public class PaymentServiceTest extends TestCore {


    @Autowired
    private PaymentService paymentService;


    @Test
    public void testSave() {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setTenantId(1111L);
        paymentService.save(paymentEntity);
    }

    @Test
    public void testCreate() {

        PaymentCreateBo paymentCreateBo = new PaymentCreateBo();
        paymentCreateBo.setOrderNo("NO:00000001");
        paymentCreateBo.setAmount(new BigDecimal("100"));

        String encrypt = SecurityUtil.encrypt("1a0b5f07fda845929e87e48f931d566a", paymentCreateBo);

        log.info(encrypt);

        PaymentBaseBo paymentBaseBo = new PaymentBaseBo();
        paymentBaseBo.setTenantId(666666L);
        paymentBaseBo.setContext(encrypt);

        paymentService.create(paymentBaseBo);
    }


}
