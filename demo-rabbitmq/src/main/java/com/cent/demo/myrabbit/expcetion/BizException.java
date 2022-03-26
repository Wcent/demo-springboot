package com.cent.demo.myrabbit.expcetion;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class BizException extends RuntimeException{

    public BizException(String msg) {
        super(msg);
    }

    public BizException(ErrMsg errMsg, Object...args) {
        super(errMsg.getMsg(args));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BizException that = (BizException) obj;
        return Objects.equals(getMessage(), that.getMessage());
    }

    @JsonValue
    public String getMessage() {
        return super.getMessage();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public enum ErrMsg {

        ERR0000("ERR0000", "未知异常"),
        ERR0404("ERR0404", "消息[%d]不存在"),
        ERR9999("ERR9999", "请求太频繁"),
        SUC0000("SUC0000", "处理成功");

        private final String code;
        private final String text;

        ErrMsg(String code, String text) {
            this.code = code;
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public String getText() {
            return text;
        }

        public String getMsg(Object...args) {
            return String.format(code + " - " + text, args);
        }
    }
}
