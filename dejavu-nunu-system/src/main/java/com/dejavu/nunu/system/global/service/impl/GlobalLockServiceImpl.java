package com.dejavu.nunu.system.global.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dejavu.nunu.system.global.entity.GlobalLockEntity;
import com.dejavu.nunu.system.global.exception.GlobalLockRepException;
import com.dejavu.nunu.system.global.mapper.GlobalLockMapper;
import com.dejavu.nunu.system.global.service.GlobalLockService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GlobalLockServiceImpl extends ServiceImpl<GlobalLockMapper, GlobalLockEntity> implements GlobalLockService {


    @Override
    public void lock(Long tenantId, String key, String type) throws GlobalLockRepException {
        GlobalLockEntity globalLockEntity = new GlobalLockEntity();
        globalLockEntity.setTenantId(tenantId);
        globalLockEntity.setKey(key);
        globalLockEntity.setType(type);
        globalLockEntity.setCreatedTime(new Date());
        try {
            baseMapper.insert(globalLockEntity);
        } catch (Exception e) {
            throw new GlobalLockRepException(e.getMessage());
        }
    }


}
