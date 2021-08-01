package com.dejavu.nunu.system.global.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dejavu.nunu.system.global.entity.GlobalLockEntity;
import com.dejavu.nunu.system.global.exception.GlobalLockRepException;

public interface GlobalLockService extends IService<GlobalLockEntity> {


    /**
     * 锁
     *
     * @param tenantId
     * @param key
     * @param type
     * @throws GlobalLockRepException
     */
    public void lock(Long tenantId, String key, String type) throws GlobalLockRepException;

}
