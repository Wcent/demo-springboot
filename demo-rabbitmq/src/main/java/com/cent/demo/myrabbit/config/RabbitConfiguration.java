package com.cent.demo.myrabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class RabbitConfiguration {

    public final static String EXCHANGE_DIRECT = "my.direct.exchange";
    public final static String EXCHANGE_DIRECT_DLX_TTL = "my.direct.exchange.dlx.ttl";
    public final static String EXCHANGE_DIRECT_DLX_DELAY = "my.direct.exchange.dlx.delay";
    public final static String EXCHANGE_TOPIC = "my.topic.exchange";
    public final static String EXCHANGE_FANOUT = "my.fanout.exchange";

    public final static String QUEUE_DEFAULT = "my.default.queue";
    public final static String QUEUE_DIRECT = "my.direct.queue";
    public final static String QUEUE_DIRECT_JSON = "my.direct.queue.json";
    public final static String QUEUE_DIRECT_DLX_TTL = "my.direct.queue.dlx.ttl";
    public final static String QUEUE_DIRECT_DLX_DELAY = "my.direct.queue.dlx.delay";
    public final static String QUEUE_TOPIC_ODD = "my.topic.queue.odd";
    public final static String QUEUE_TOPIC_EVEN = "my.topic.queue.even";
    public final static String QUEUE_FANOUT_ONE = "my.fanout.queue.one";
    public final static String QUEUE_FANOUT_TWO = "my.fanout.queue.two";
    public final static String QUEUE_FANOUT_ACK_MANUAL = "my.fanout.queue.ack.manual";

    public final static String BINDING_DIRECT = QUEUE_DIRECT;
    public final static String BINDING_DIRECT_JSON = QUEUE_DIRECT_JSON;
    public final static String BINDING_DIRECT_DLX_TTL = QUEUE_DIRECT_DLX_TTL;
    public final static String BINDING_DIRECT_DLX_DELAY = QUEUE_DIRECT_DLX_DELAY;
    public final static String BINDING_TOPIC_ODD = "my.topic.odd.#";
    public final static String BINDING_TOPIC_EVEN = "my.topic.even.#";

    public final static String ROUTING_DEFAULT = QUEUE_DEFAULT;
    public final static String ROUTING_DIRECT = BINDING_DIRECT;
    public final static String ROUTING_DIRECT_JSON = BINDING_DIRECT_JSON;
    public final static String ROUTING_DIRECT_DLX_TTL = BINDING_DIRECT_DLX_TTL;
    public final static String ROUTING_DIRECT_DLX_DELAY = BINDING_DIRECT_DLX_DELAY;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_DIRECT);
    }

    @Bean
    public DirectExchange directDlxTtlExchange() {
        return new DirectExchange(EXCHANGE_DIRECT_DLX_TTL);
    }

    @Bean
    public DirectExchange directDlxDelayExchange() {
        return new DirectExchange(EXCHANGE_DIRECT_DLX_DELAY);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_TOPIC);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_FANOUT);
    }

    @Bean
    public Queue directQueue() {
        return new Queue(QUEUE_DIRECT);
    }

    @Bean
    public Queue directJsonQueue() {
        return new Queue(QUEUE_DIRECT_JSON);
    }

    @Bean
    public Queue directDlxTtlQueue() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 3*60*1000);
        args.put("x-dead-letter-exchange", EXCHANGE_DIRECT_DLX_DELAY);
        args.put("x-dead-letter-routing-key", ROUTING_DIRECT_DLX_DELAY);
        return new Queue(QUEUE_DIRECT_DLX_TTL, true, false, false, args);
    }

    @Bean
    public Queue directDlxDelayQueue() {
        return new Queue(QUEUE_DIRECT_DLX_DELAY);
    }

    @Bean
    public Queue oddTopicQueue() {
        return new Queue(QUEUE_TOPIC_ODD);
    }

    @Bean
    public Queue evenTopicQueue() {
        return new Queue(QUEUE_TOPIC_EVEN);
    }

    @Bean
    public Queue fanoutOneQueue() {
        return new Queue(QUEUE_FANOUT_ONE);
    }

    @Bean
    public Queue fanoutTwoQueue() {
        return new Queue(QUEUE_FANOUT_TWO);
    }

    @Bean
    public Queue fanoutAckManualQueue() {
        return new Queue(QUEUE_FANOUT_ACK_MANUAL);
    }

    @Bean
    public Queue defaultQueue() {
        return new Queue(QUEUE_DEFAULT);
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(BINDING_DIRECT);
    }

    @Bean
    public Binding directJsonBinding() {
        return BindingBuilder.bind(directJsonQueue()).to(directExchange()).with(BINDING_DIRECT_JSON);
    }

    @Bean
    public Binding directDlxTtlBinding() {
        return BindingBuilder.bind(directDlxTtlQueue()).to(directDlxTtlExchange()).with(BINDING_DIRECT_DLX_TTL);
    }

    @Bean
    public Binding directDlxDelayBinding() {
        return BindingBuilder.bind(directDlxDelayQueue()).to(directDlxDelayExchange()).with(BINDING_DIRECT_DLX_DELAY);
    }

    @Bean
    public Binding oddTopicBinding() {
        return BindingBuilder.bind(oddTopicQueue()).to(topicExchange()).with(BINDING_TOPIC_ODD);
    }

    @Bean
    public Binding evenTopicBinding() {
        return BindingBuilder.bind(evenTopicQueue()).to(topicExchange()).with(BINDING_TOPIC_EVEN);
    }

    @Bean
    public Binding fanoutOneBinding() {
        return BindingBuilder.bind(fanoutOneQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutTwoBinding() {
        return BindingBuilder.bind(fanoutTwoQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutAckManualBinding() {
        return BindingBuilder.bind(fanoutAckManualQueue()).to(fanoutExchange());
    }
}
