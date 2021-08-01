package com.dejavu.nunu.system.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dejavu.nunu.system.tenant.entity.TenantEntity;
import com.dejavu.nunu.system.tenant.mapper.TenantMapper;
import com.dejavu.nunu.system.tenant.service.TenantService;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, TenantEntity> implements TenantService {


    @Override
    public TenantEntity getByTenantId(Long tenantId) {
        if(null == tenantId || tenantId <= 0){
            return null;
        }
        QueryWrapper<TenantEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TenantEntity::getId,tenantId);
        return baseMapper.selectOne(queryWrapper);
    }
}
