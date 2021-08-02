package com.dejavu.nunu.system.payment.controller;

import com.dejavu.nunu.core.model.Result;
import com.dejavu.nunu.system.payment.constant.PaymentConstant;
import com.dejavu.nunu.system.payment.model.PaymentBaseBo;
import com.dejavu.nunu.system.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {


    @Autowired
    private PaymentService paymentService;

    @GetMapping("/test")
    public Result test() {
        return Result.success();
    }

    @PostMapping("/{type}")
    public Result payment(@PathVariable("type") String type, @RequestBody PaymentBaseBo paymentBaseBo) {
        Long tenantId = paymentBaseBo.getTenantId();
        if (null == tenantId || tenantId <= 0) {
            return Result.fail();
        }
        //创建支付信息
        if (PaymentConstant.Type.CREATE.equals(type)) {
            paymentService.create(paymentBaseBo);
        }
        //确认支付信息
        if (PaymentConstant.Type.CONFIRM.equals(type)) {
            paymentService.confirm(paymentBaseBo);
        }
        return Result.success();
    }


}
