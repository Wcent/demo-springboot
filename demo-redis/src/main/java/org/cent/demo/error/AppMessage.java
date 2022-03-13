package org.cent.demo.error;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public enum AppMessage {

    SUC0000("SUC0000", "处理成功"),
    ERR0000("ERR0000", "未知异常"),
    ERR0404("ERR0404", "消息[%d]不存在"),
    ERR9999("ERR9999", "请求太频繁");

    private final String code;
    private final String text;

    AppMessage(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText(Object...args) {
        return String.format(text, args);
    }

    public String getMsg(Object...args) {
        return String.format(code + " - " + text, args);
    }

}
