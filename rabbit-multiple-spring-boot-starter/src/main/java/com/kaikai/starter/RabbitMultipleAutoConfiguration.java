package com.kaikai.starter;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.support.RetryTemplate;

import java.time.Duration;
import java.util.stream.Collectors;

/**
 * rabbitMultipleAutoConfiguration
 *
 * @author pu
 * @date 2020/12/25
 * time 9:56
 */
@Configuration
@EnableConfigurationProperties({FirstRabbitMultipleProperties.class,SecondRabbitMultipleProperties.class})
@ConditionalOnMissingBean(ConnectionFactory.class)
public  class RabbitMultipleAutoConfiguration {



    @Bean(name="firstConnectionFactory")
    @Primary
    public ConnectionFactory hospSyncConnectionFactory(FirstRabbitMultipleProperties firstRabbitMultipleProperties,ObjectProvider<ConnectionNameStrategy> connectionNameStrategy){
        return getCachingConnectionFactory(firstRabbitMultipleProperties,connectionNameStrategy);
    }

    @Bean(name="secondConnectionFactory")
    public ConnectionFactory hPayConnectionFactory( SecondRabbitMultipleProperties secondRabbitMultipleProperties,ObjectProvider<ConnectionNameStrategy> connectionNameStrategy){
        return getCachingConnectionFactory(secondRabbitMultipleProperties,connectionNameStrategy);
    }
    @Bean(name="firstRabbitTemplate")
    @Primary
    public RabbitTemplate firstRabbitTemplate(
            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory,
            ObjectProvider<RabbitRetryTemplateCustomizer> retryTemplateCustomizers,
            FirstRabbitMultipleProperties properties
    ){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //使用外部事物
        //ydtRabbitTemplate.setChannelTransacted(true);
        if (properties.getRetry().isEnabled()) {
            rabbitTemplate.setRetryTemplate(new RetryTemplateFactory(
                    retryTemplateCustomizers.orderedStream()
                            .collect(Collectors.toList())).createRetryTemplate(
                    properties.getRetry(),

                    RabbitRetryTemplateCustomizer.Target.SENDER));
        }
        return rabbitTemplate;
    }

    @Bean(name="secondRabbitTemplate")
    public RabbitTemplate secondRabbitTemplate(
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory,
            ObjectProvider<RabbitRetryTemplateCustomizer> retryTemplateCustomizers,
            SecondRabbitMultipleProperties properties
    ){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //使用外部事物
        //lpzRabbitTemplate.setChannelTransacted(true);
        if (properties.getRetry().isEnabled()) {
            rabbitTemplate.setRetryTemplate(new RetryTemplateFactory(
                    retryTemplateCustomizers.orderedStream()
                            .collect(Collectors.toList())).createRetryTemplate(
                    properties.getRetry(),
                    RabbitRetryTemplateCustomizer.Target.SENDER));
        }
        return rabbitTemplate;
    }

    @Bean(name="firstRabbitListenerContainerFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory hospSyncFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory,
            FirstRabbitMultipleProperties firstRabbitMultipleProperties
    ) {
        return getSimpleRabbitListenerContainerFactory(configurer,connectionFactory,firstRabbitMultipleProperties);
    }


    @Bean(name="secondRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory hPayFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory,SecondRabbitMultipleProperties secondRabbitMultipleProperties
    ) {
       return getSimpleRabbitListenerContainerFactory(configurer,connectionFactory,secondRabbitMultipleProperties);
    }
    @Bean("firstAmqpAdmin")
    @Primary
//    @ConditionalOnSingleCandidate(ConnectionFactory.class)
    @ConditionalOnProperty(prefix = "spring.rabbit.multiple.first", name = "dynamic", matchIfMissing = true)
    public AmqpAdmin firstAmqpAdmin(@Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
    @Bean("secondAmqpAdmin")
//    @ConditionalOnSingleCandidate(ConnectionFactory.class)
    @ConditionalOnProperty(prefix = "spring.rabbit.multiple.second", name = "dynamic", matchIfMissing = true)
    public AmqpAdmin secondAmqpAdmin(@Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    private  CachingConnectionFactory getCachingConnectionFactory(RootRabbitMultipleProperties rootRabbitMultipleProperties,ObjectProvider<ConnectionNameStrategy> connectionNameStrategy){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        PropertyMapper map = PropertyMapper.get();
        connectionFactory.setHost(rootRabbitMultipleProperties.getHost());
        connectionFactory.setPort(rootRabbitMultipleProperties.getPort());
        connectionFactory.setUsername(rootRabbitMultipleProperties.getUsername());
        connectionFactory.setPassword(rootRabbitMultipleProperties.getPassword());
        connectionFactory.setVirtualHost(rootRabbitMultipleProperties.getVirtualHost());
        connectionFactory.setPublisherReturns(rootRabbitMultipleProperties.isPublisherReturns());
        connectionFactory.setPublisherConfirms(rootRabbitMultipleProperties.isPublisherConfirms());
        RabbitProperties.Cache.Channel channel = rootRabbitMultipleProperties.getCache().getChannel();
        map.from(channel::getSize).whenNonNull().to(connectionFactory::setChannelCacheSize);
        map.from(channel::getCheckoutTimeout).whenNonNull().as(Duration::toMillis)
                .to(connectionFactory::setChannelCheckoutTimeout);
        RabbitProperties.Cache.Connection connection = rootRabbitMultipleProperties.getCache()
                .getConnection();
        map.from(connection::getMode).whenNonNull().to(connectionFactory::setCacheMode);
        map.from(connection::getSize).whenNonNull()
                .to(connectionFactory::setConnectionCacheSize);
        map.from(connectionNameStrategy::getIfUnique).whenNonNull()
                .to(connectionFactory::setConnectionNameStrategy);
        return connectionFactory;
    }

    private SimpleRabbitListenerContainerFactory getSimpleRabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory,RootRabbitMultipleProperties rootRabbitMultipleProperties){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setAcknowledgeMode(rootRabbitMultipleProperties.getAckModel());
        factory.setConcurrentConsumers(rootRabbitMultipleProperties.getConsumerNumber());
        // 最大消费者数量
        factory.setMaxConcurrentConsumers(rootRabbitMultipleProperties.getMaxConsumerNumber());
        // 最大投递数
        factory.setPrefetchCount(rootRabbitMultipleProperties.getPrefetchCount());
        return factory;
    }

}
