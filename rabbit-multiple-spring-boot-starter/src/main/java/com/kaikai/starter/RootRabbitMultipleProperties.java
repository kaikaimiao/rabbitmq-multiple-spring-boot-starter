package com.kaikai.starter;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

/**
 * RootRabbitMultipleProperties
 *
 * @author pu
 * @date 2020/12/25
 * time 9:57
 */
public class RootRabbitMultipleProperties {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String virtualHost;

    private  Integer maxConsumerNumber=10;

    private  Integer consumerNumber=5;

    private AcknowledgeMode ackModel=AcknowledgeMode.AUTO;

    /**
     * Whether to enable publisher confirms.
     */
    private boolean publisherConfirms;

    /**
     * Whether to enable publisher returns.
     */
    private boolean publisherReturns;

    private RabbitProperties.Cache cache = new RabbitProperties.Cache();

    public RabbitProperties.Cache getCache() {
        return cache;
    }

    public void setCache(RabbitProperties.Cache cache) {
        this.cache = cache;
    }

    private RabbitProperties.Retry retry=new RabbitProperties.Retry();

    public RabbitProperties.Retry getRetry() {
        return retry;
    }

    public void setRetry(RabbitProperties.Retry retry) {
        this.retry = retry;
    }

    public Integer getMaxConsumerNumber() {
        return maxConsumerNumber;
    }

    public void setMaxConsumerNumber(Integer maxConsumerNumber) {
        this.maxConsumerNumber = maxConsumerNumber;
    }

    public Integer getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(Integer consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public AcknowledgeMode getAckModel() {
        return ackModel;
    }

    public void setAckModel(AcknowledgeMode ackModel) {
        this.ackModel = ackModel;
    }

    public Integer getPrefetchCount() {
        return prefetchCount;
    }

    public void setPrefetchCount(Integer prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    private  Integer prefetchCount=1;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public boolean isPublisherConfirms() {
        return publisherConfirms;
    }

    public void setPublisherConfirms(boolean publisherConfirms) {
        this.publisherConfirms = publisherConfirms;
    }

    public boolean isPublisherReturns() {
        return publisherReturns;
    }

    public void setPublisherReturns(boolean publisherReturns) {
        this.publisherReturns = publisherReturns;
    }
}
