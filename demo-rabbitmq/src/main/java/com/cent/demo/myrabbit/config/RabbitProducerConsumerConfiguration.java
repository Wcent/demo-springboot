package com.cent.demo.myrabbit.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.CorrelationDataPostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

@Configuration
@Slf4j
public class RabbitProducerConsumerConfiguration implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;

    @PostConstruct
    public void init() {
        Jackson2JsonMessageConverter jsonMessageConverter = jackson2JsonMessageConverter();

        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
        rabbitTemplate.setCorrelationDataPostProcessor((message, correlationData) -> {
            message.getMessageProperties().setCorrelationId(correlationData.getId());
//            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return correlationData;
        });

        // 消费反序列化json，感觉不如直接string接收json串再处理（未找到原因C端string/object同时出现出时灵时不灵）
        // 接收参数推断，不准
//        jsonMessageConverter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        // 消息头推断，不准
//        jsonMessageConverter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        simpleRabbitListenerContainerFactory.setMessageConverter(jsonMessageConverter);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.debug("[P->X->Q:confirm:X->P]:correlation={}, ack={}, cause={}", correlationData, ack, cause);
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.debug("[P->X!->Q:return:X->P]:{}", returnedMessage);
    }

    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(getMyObjectMapper());
    }

    public ObjectMapper getMyObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 默认所有属性访问器方法、所有访问修饰方法或属性可见
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 序列化非final类型值时，包装为带类型json，自然类型不带类型信息
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_OBJECT);

        // 序列化、反序列化，未知属性忽略报错
        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 使用toString()方法序列化、反序列化枚举类型
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

        // 支持JDK8日期时间序列化、反序列化处理
        objectMapper.registerModule(new JavaTimeModule());
        // 日期时间序列化为字符串格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 持续时间序列化为字符串格式
        objectMapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        // 改变默认日期时间数值序列化方式[yyyy, MM, dd, HH, mm, ss]，避免与默认字符串反序列化方式"yyyy-MM-ddTHH:mm:ss"不一致异常
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 设置序列化、反序列化日期时间字符串格式（好像不起作用）
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 设置中国时区
        objectMapper.setTimeZone(SimpleTimeZone.getTimeZone("GMT+8"));

        return objectMapper;
    }
}
