package com.cent.demo.msdatasource.mapper.master;

import com.cent.demo.msdatasource.domain.SysCtlDO;

/**
 * 主库数据源DAO接口
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
public interface SysCtlMasterDAO {
    /**
     * 新增系统控制配置
     * @param sysCtlDO 系统配置领域实体类对象
     * @return 影响记录数
     */
    int insertSysCtl(SysCtlDO sysCtlDO);

    /**
     * 更新系统控制配置状态
     * @param sysCtlDO 系统配置领域实体类对象
     * @return 影响记录数
     */
    int updateSysCtlStatus(SysCtlDO sysCtlDO);

    /**
     * 删除系统控制配置
     * @param sysCtlDO 系统配置领域实体类对象
     * @return 影响记录数
     */
    int deleteSysCtl(SysCtlDO sysCtlDO);
}
