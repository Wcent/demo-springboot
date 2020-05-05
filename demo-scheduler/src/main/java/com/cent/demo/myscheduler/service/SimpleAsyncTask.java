package com.cent.demo.myscheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 简单异步任务
 * 注解@Async限定类，则类对象所有方法被调用时皆为异步处理
 *          限定方法，则仅指定方法在被调用时会异步处理
 *
 * @author Vincent
 * @version 1.0 2020/5/5
 */
@Slf4j
@Service
@Async
public class SimpleAsyncTask {

    /**
     * 模拟异步处理任务
     */
    public void runAsync() {
        log.info("异步任务由线程{}处理", Thread.currentThread());
        // 注意同一异步任务类内部直接调用，异步无效，由调用的当前线程同步执行处理
        innerAsyncInvalid();
    }

    /**
     * 模拟异步任务执行异常终止
     */
    public void runAsyncException() {
        log.warn("模拟异步任务由线程{}执行时异常终止", Thread.currentThread());
        throw new RuntimeException("模拟异步任务执行异常终止");
    }

    /**
     * 模拟被同一异步类内部直接调用处理，异步无效，由调用的当前线程同步执行处理
     */
    public void innerAsyncInvalid() {
        log.warn("同一类内部直接调用异步无效，任务由线程{}处理", Thread.currentThread());
    }
}
