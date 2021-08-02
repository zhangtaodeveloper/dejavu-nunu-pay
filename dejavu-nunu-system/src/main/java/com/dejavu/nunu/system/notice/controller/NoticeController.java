package com.dejavu.nunu.system.notice.controller;

import com.dejavu.nunu.core.model.Result;
import com.dejavu.nunu.system.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
public class NoticeController {


    @Autowired
    private NoticeService noticeService;


    /**
     * 确认收款回调通知
     *
     * @param data
     * @return
     */
    @GetMapping("/confirm/{tenantId}/{data}")
    public Result payment(@PathVariable("tenantId") Long tenantId, @PathVariable("data") String data) {
        noticeService.confirm(tenantId, data);
        return Result.success();
    }


}
