# rabbitmq-multiple-spring-boot-starter
用于解决springboot连接多个rabbitmq的starter
/**
 * rabbit1配置
 *
 * @author kaikai
 */
@Configuration
public class FirstRabbitConfig {

    @Autowired
    @Qualifier("firstAmqpAdmin")
    private AmqpAdmin amqpAdmin;


    @PostConstruct
    private void bind() {
        RabbitUtil.createQueueBindTopicExchange(amqpAdmin, RabbitConstant.TOPIC_EXCHANGE, RabbitConstant.SALARY_QUEUE_NAME, RabbitConstant.SALARY_ROUTING_KEY);
        RabbitUtil.createQueueBindTopicExchange(amqpAdmin, RabbitConstant.TOPIC_EXCHANGE, RabbitConstant.MONTH_SALARY_QUEUE_NAME, RabbitConstant.MONTH_SALARY_ROUTING_KEY);
    }


}
