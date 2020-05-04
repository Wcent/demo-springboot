package com.cent.demo.dydatasource.mapper.read;

import com.cent.demo.dydatasource.annotation.UseDataSource;
import com.cent.demo.dydatasource.config.DynamicDataSource;
import com.cent.demo.dydatasource.domain.SysCtlDO;

import java.util.List;

/**
 * 读库数据源DAO接口
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
public interface SysCtlReadableDAO {

    /**
     * 查询系统配置
     * @param name 配置名称
     * @return 系统配置
     */
    @UseDataSource(type = DynamicDataSource.DataSourceType.Readable)
    SysCtlDO getSysCtl(String name);

    /**
     * 查询系统配置列表
     * @return 系统配置列表
     */
    @UseDataSource(type = DynamicDataSource.DataSourceType.Readable)
    List<SysCtlDO> listSysCtl();
}
