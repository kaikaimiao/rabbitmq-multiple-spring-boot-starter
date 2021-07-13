# rabbitmq-multiple-spring-boot-starter
用于解决springboot连接多个rabbitmq的starter

/**
 * rabbit2配置
 *
 * @author kaikai
 */
@Configuration
public class SecondRabbitConfig {

    @Autowired
    @Qualifier("secondAmqpAdmin")
    private AmqpAdmin amqpAdmin;


    @PostConstruct
    private void bind() {
        //调用上面的工具类
        RabbitUtil.createQueueBindTopicExchange(amqpAdmin, RabbitConstant.TOPIC_EXCHANGE, RabbitConstant.SALARY_QUEUE_NAME, RabbitConstant.SALARY_ROUTING_KEY);
        RabbitUtil.createQueueBindTopicExchange(amqpAdmin, RabbitConstant.TOPIC_EXCHANGE, RabbitConstant.MONTH_SALARY_QUEUE_NAME, RabbitConstant.MONTH_SALARY_ROUTING_KEY);
    }

}
