package com.cent.demo.myrabbit.controller;

import com.cent.demo.myrabbit.config.RabbitConfiguration;
import com.cent.demo.myrabbit.expcetion.BizException;
import com.cent.demo.myrabbit.model.Notify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@Slf4j
public class RabbitController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/myrabbit/{type}")
    public String sendMsg(@PathVariable("type") String type) {
        log.debug("Testing send a msg via rabbitmq:{}", type);
        LocalDateTime now = LocalDateTime.now();
        String strMsg = "a string msg at " + now;
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        switch (type) {
            case "direct":
                rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DIRECT, RabbitConfiguration.ROUTING_DIRECT,
                        strMsg, correlationData);
                break;
            case "topic":
                if (now.getSecond() % 2 == 0) {
                    rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_TOPIC, "my.topic.even."+now,
                            strMsg, correlationData);
                } else {
                    rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_TOPIC, "my.topic.odd."+now,
                            strMsg, correlationData);
                }
                break;
            case "fanout":
                rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_FANOUT, "fanout.without.routing.key",
                        strMsg, correlationData);
                break;
            case "notify":
                rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DIRECT, RabbitConfiguration.ROUTING_DIRECT_JSON,
                        mockNotify(), correlationData);
                break;
            case "404":
                rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DIRECT, "direct.queue.404.not.found",
                        strMsg, correlationData);
                break;
            case "delay":
                rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DIRECT_DLX_TTL, RabbitConfiguration.ROUTING_DIRECT_DLX_TTL,
                        strMsg, correlationData);
                break;
            default:
                // 发送消息到默认dirt交换机（根据队列名称路由送达绑定队列）
                rabbitTemplate.convertAndSend(RabbitConfiguration.ROUTING_DEFAULT, (Object) strMsg, correlationData);
        }
        return "done";
    }

    private Notify mockNotify() {
        return Notify.builder()
                .id(123)
                .subscriber("notify")
                .message("testing spring rabbit")
                .errorCode(BizException.ErrMsg.SUC0000.getCode())
                .errorText(BizException.ErrMsg.SUC0000.getMsg())
                .status(new Byte("0"))
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .version(1)
                .build();
    }
}
