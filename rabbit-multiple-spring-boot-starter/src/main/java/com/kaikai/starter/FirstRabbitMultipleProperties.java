package com.kaikai.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FirstRabbitMultipleProperties
 *
 * @author pu
 * @date 2020/12/25
 * time 10:01
 */
@ConfigurationProperties(prefix = "spring.rabbit.multiple.first")
public class FirstRabbitMultipleProperties extends  RootRabbitMultipleProperties {


}
