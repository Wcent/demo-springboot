package com.cent.demo.msdatasource.mapper.slave;

import com.cent.demo.msdatasource.domain.SysCtlDO;

import java.util.List;

/**
 * 从库数据源DAO接口
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
public interface SysCtlSlaveDAO {

    /**
     * 查询系统配置
     * @param name 配置名称
     * @return 系统配置
     */
    SysCtlDO getSysCtl(String name);

    /**
     * 查询系统配置列表
     * @return 系统配置列表
     */
    List<SysCtlDO> listSysCtl();
}
