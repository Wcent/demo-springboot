package com.cent.demo.myrabbit.component.rabbitmq;

import com.cent.demo.myrabbit.config.RabbitConfiguration;
import com.cent.demo.myrabbit.model.Notify;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class RabbitConsumer {

    private final ConcurrentHashMap<String, AtomicInteger> msgRetryCntCache = new ConcurrentHashMap<>();

    @RabbitListener(queues = RabbitConfiguration.QUEUE_DIRECT)
    public void consumeViaDirectQueue(String msg) {
        consume(RabbitConfiguration.QUEUE_DIRECT, msg);
    }

    // 消费反序列化json，感觉不如直接string接收json串再处理（未找到原因C端string/object同时出现出时灵时不灵）
    // 可以分类使用，避免歧义
    @RabbitListener(queues = RabbitConfiguration.QUEUE_DIRECT_JSON)
    public void consumeNotifyViaDirectQueue(Notify msg, Message message) {
        log.debug("接收[{}]消息：{}", message.getMessageProperties().getConsumerQueue(), msg);
    }

    // DLX不消费并设置TTL，消息过期DLX-TTL -> DLX-DELAY，延迟消费
    @RabbitListener(queues = RabbitConfiguration.QUEUE_DIRECT_DLX_DELAY)
    public void consumeViaDirectDlxDelayQueue(String msg, Message message) {
        log.debug("接收[{}]消息：{}", message.getMessageProperties().getConsumerQueue(), msg);
    }

    @RabbitListener(queues = {RabbitConfiguration.QUEUE_TOPIC_ODD, RabbitConfiguration.QUEUE_TOPIC_EVEN})
    public void consumeViaTopicQueue(String msg, Message message) {
        log.debug("消息：id={}, exchange={}, routing={}", message.getMessageProperties().getCorrelationId(),
                message.getMessageProperties().getReceivedExchange(),
                message.getMessageProperties().getReceivedRoutingKey());
        consume(message.getMessageProperties().getConsumerQueue(), msg);
    }

    @RabbitListener(queues = RabbitConfiguration.QUEUE_FANOUT_ONE)
    public void consumeViaFanoutOneQueue(String msg) {
        consume(RabbitConfiguration.QUEUE_FANOUT_ONE, msg);
    }

    @RabbitListener(queues = RabbitConfiguration.QUEUE_FANOUT_TWO)
    public void consumeViaFanoutTwoQueue(String msg) {
        consume(RabbitConfiguration.QUEUE_FANOUT_TWO, msg);
    }

    @RabbitListener(queues = RabbitConfiguration.QUEUE_FANOUT_ACK_MANUAL, ackMode = "MANUAL")
    public void consumeViaFanoutAckManualQueue(String msg, Channel channel,
                                           @Header(AmqpHeaders.CORRELATION_ID) String correlationId,
                                           @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
        consume(RabbitConfiguration.QUEUE_FANOUT_ACK_MANUAL, msg);
        try {
            AtomicInteger retryCnt = msgRetryCntCache.getOrDefault(correlationId, new AtomicInteger(0));
            log.debug("correlationId={}, deliveryTag={}, cnt={}", correlationId, deliveryTag, retryCnt);
            int cnt = retryCnt.incrementAndGet();
            if (cnt <= 3) {
                log.warn("模拟消息manual-nack应答，并主动使消息重入队列，correlationId={}, deliveryTag={}, cnt={}",
                        correlationId, deliveryTag, retryCnt);
                msgRetryCntCache.put(correlationId, retryCnt);
                // 注意manual-nack会阻塞队列，影响消费，特别要避免无限消息循环阻塞（一直重回队头），考虑超限不入队或进死信队列
                channel.basicNack(deliveryTag, false, true);
                return;
            }
            if (cnt < 6) {
                log.error("模拟消费异常，触发执行配置重试retry消费策略，correlationId={}, deliveryTag={}, cnt={}",
                        correlationId, deliveryTag, retryCnt);
                msgRetryCntCache.put(correlationId, retryCnt);
                // 注意retry重试会阻塞队列，影响消费，特别要避免无限消息循环阻塞（一直重回队头），考虑超限不入队或进死信队列
                throw new RuntimeException("抛出异常，触发执行配置的retry策略消费重试");
            }
            if (cnt == 6) {
                log.debug("模拟消息manual-ack应答，正常消费应答结束，correlationId={}, deliveryTag={}, cnt={}", correlationId, deliveryTag, retryCnt);
                channel.basicAck(deliveryTag, false);
                msgRetryCntCache.remove(correlationId);
            } else {
                // 要避免无限消息循环阻塞（一直重回队头），考虑超限不入队或进死信队列
                log.warn("模拟消息manual-reject应答，超限拒绝重入队再消费结束，correlationId={}, deliveryTag={}, cnt={}", correlationId, deliveryTag, retryCnt);
                channel.basicReject(deliveryTag, false);
                msgRetryCntCache.remove(correlationId);
            }
        } catch (IOException e) {
            log.error(String.format("消息[%s]确认异常", deliveryTag), e);
//            try {
//                // 应答异常，再试试拒绝消费，不重新入队了
//                channel.basicReject(deliveryTag, false);
//            } catch (IOException ex) {
//                log.error(String.format("消息[%s]拒绝异常", deliveryTag), ex);
//            }
        }
    }

    @RabbitListener(queues = RabbitConfiguration.QUEUE_DEFAULT)
    public void consumeViaDefaultQueue(String msg) {
        consume(RabbitConfiguration.QUEUE_DEFAULT, msg);
    }

    private void consume(String type, String msg) {
        log.debug("接收[{}]消息：{}", type, msg);
    }
}
