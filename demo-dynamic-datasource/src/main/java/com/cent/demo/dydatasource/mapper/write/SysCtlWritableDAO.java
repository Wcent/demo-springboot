package com.cent.demo.dydatasource.mapper.write;


import com.cent.demo.dydatasource.annotation.UseDataSource;
import com.cent.demo.dydatasource.config.DynamicDataSource;
import com.cent.demo.dydatasource.domain.SysCtlDO;

import java.util.List;

/**
 * 写库数据源DAO接口
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
public interface SysCtlWritableDAO {
    /**
     * 新增系统控制配置
     * @param sysCtlDO 系统配置领域实体类对象
     * @return 影响记录数
     */
    @UseDataSource(type = DynamicDataSource.DataSourceType.Writable)
    int insertSysCtl(SysCtlDO sysCtlDO);

    /**
     * 更新系统控制配置状态
     * @param sysCtlDO 系统配置领域实体类对象
     * @return 影响记录数
     */
    @UseDataSource(type = DynamicDataSource.DataSourceType.Writable)
    int updateSysCtlStatus(SysCtlDO sysCtlDO);

    /**
     * 删除系统控制配置
     * @param sysCtlDO 系统配置领域实体类对象
     * @return 影响记录数
     */
    @UseDataSource(type = DynamicDataSource.DataSourceType.Writable)
    int deleteSysCtl(SysCtlDO sysCtlDO);

    /**
     * 查询系统配置
     * @param name 配置名称
     * @return 系统配置
     */
    @UseDataSource(type = DynamicDataSource.DataSourceType.Writable)
    SysCtlDO getSysCtl(String name);

    /**
     * 查询系统配置列表
     * @return 系统配置列表
     */
    @UseDataSource(type = DynamicDataSource.DataSourceType.Writable)
    List<SysCtlDO> listSysCtl();
}
