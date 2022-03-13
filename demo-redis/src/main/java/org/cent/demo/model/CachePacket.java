package org.cent.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class CachePacket {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime cacheTime = LocalDateTime.now();

    Object payload;

    public CachePacket() {}

    public CachePacket(Object data) {
        payload = data;
    }

    public LocalDateTime getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(LocalDateTime cacheTime) {
        this.cacheTime = cacheTime;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "CachePacket{" +
                "cacheTime=" + cacheTime +
                ", payload=" + payload +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CachePacket that = (CachePacket) o;
        return Objects.equals(cacheTime, that.cacheTime) && Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cacheTime, payload);
    }

}
