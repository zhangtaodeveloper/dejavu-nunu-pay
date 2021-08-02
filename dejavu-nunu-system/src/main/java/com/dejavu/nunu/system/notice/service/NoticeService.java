package com.dejavu.nunu.system.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dejavu.nunu.system.notice.entity.NoticeEntity;

public interface NoticeService extends IService<NoticeEntity> {

    /**
     * 通知
     *
     * @param orderNo
     */
    public void send(String orderNo);

}
