package com.dejavu.nunu.system.tenant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dejavu.nunu.system.tenant.entity.TenantEntity;

public interface TenantService extends IService<TenantEntity> {

 
    /**
     * 查询租户
     *
     * @param tenantId 租户ID
     * @return
     */
    public TenantEntity getByTenantId(Long tenantId);

}
