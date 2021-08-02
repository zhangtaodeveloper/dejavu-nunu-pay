package com.dejavu.nunu.system.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dejavu.nunu.system.notice.constant.NoticeStatusEnum;
import com.dejavu.nunu.system.notice.entity.NoticeEntity;

public interface NoticeService extends IService<NoticeEntity> {

    /**
     * 通知
     *
     * @param orderNo
     */
    public void send(String orderNo);

    /**
     * 更新状态
     *
     * @param noticeId
     * @param status
     */
    public void updateStatus(Long noticeId, NoticeStatusEnum status);

    /**
     * 确认收款回调通知
     *
     * @param data
     */
    public void confirm(Long tenantId, String data);

}
