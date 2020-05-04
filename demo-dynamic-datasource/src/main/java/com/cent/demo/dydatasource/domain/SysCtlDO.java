package com.cent.demo.dydatasource.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统控制配置
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
@Data
@Builder
public class SysCtlDO {
    private long id;
    private String name;
    private String ctlValue;
    private int status;
    private int version;
    private LocalDateTime mntTime;
    private LocalDateTime addTime;
}
