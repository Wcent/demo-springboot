package com.cent.demo.dydatasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 自定义动态数据源，继承spring路由数据源，实现动态选择数据源处理
 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
 *
 * @author Vincent
 * @version 1.0 2020/5/4
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 本地线程数据源持有者，隔离不同线程上下文使用的数据源，保证线程安全
     */
    private static final ThreadLocal<DataSourceType> DATA_SOURCE_CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 确定当前线程上下文设置的数据源key
     * @return 数据源类型
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    /**
     * 设置当前线程使用的数据源
     * @param type 数据源类型
     */
    public static void setDataSource(DataSourceType type) {
        DATA_SOURCE_CONTEXT_HOLDER.set(type);
    }

    /**
     * 获取当前线程使用的数据源
     * @return 数据源类型
     */
    private static DataSourceType getDataSource() {
        return DATA_SOURCE_CONTEXT_HOLDER.get();
    }

    /**
     * 清除当前线程上下文设置的数据源
     */
    public static void clear() {
        DATA_SOURCE_CONTEXT_HOLDER.remove();
    }

    /**
     * 数据源类型
     */
    public enum DataSourceType {
        /**
         * 写库
         */
        Writable,

        /**
         * 读库
         */
        Readable;
    }
}
