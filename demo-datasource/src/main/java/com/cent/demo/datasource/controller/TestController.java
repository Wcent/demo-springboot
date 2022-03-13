package com.cent.demo.datasource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试使用
 *
 * @author Vincent
 * @version 1.0 2020/8/8
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello World!";
    }
}
