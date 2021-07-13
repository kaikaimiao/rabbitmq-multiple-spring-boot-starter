# rabbitmq-multiple-spring-boot-starter
用于解决springboot连接多个rabbitmq的starter


@Component
public class Producer{
     /**
     * rabbit-one的发送对象
     */
    @Autowired
    @Qualifier("firstRabbitTemplate")
    private RabbitTemplate firstRabbitTemplate;
    
    
     /**
     * rabbit-second的发送对象
     */
    @Autowired
    @Qualifier("secondRabbitTemplate")
    private RabbitTemplate secondRabbitTemplate;
    

}
