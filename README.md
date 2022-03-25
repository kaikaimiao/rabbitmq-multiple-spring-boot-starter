# rabbitmq-multiple-spring-boot-starter
用于解决springboot连接多个rabbitmq的starter

## 1.使用前先对项目进行打包 

## 2.application.yaml的配置如下
```
spring:
  rabbit:
    multiple:
    rabbit1
      first:
        host: 192.168.0.12
        port: 5672
        username: rabbit1
        password: 123456
        virtual-host: /
        ackModel: MANUAL
        publisher-confirms: true
        publisher-returns: true
    rabbit2
      second:
        host: 192.168.0.12
        port: 5672
        username: rabbit2
        password: 123456
        virtual-host: /
        ackModel: MANUAL
        publisher-confirms: true
        publisher-returns: true
```        
        
        

