# rabbitmq-multiple-spring-boot-starter
用于解决springboot连接多个rabbitmq的starter
/**
 * rabbit工具类
 *
 * @author pu
 * @date 2021/3/10
 * time 14:07
 */
public class RabbitUtil {
    /**
     *
     * @param amqpAdmin mq管理对象
     * @param topicName 交换机名
     * @param queueName 队列名
     * @param routingKey 路由key
     */
    public  static void  createQueueBindTopicExchange(AmqpAdmin amqpAdmin,String topicName,String queueName,String routingKey){
        TopicExchange topicExchange = new TopicExchange(topicName);
        amqpAdmin.declareExchange(topicExchange);
        Queue testQueue = new Queue(queueName,true);
        Binding bind = BindingBuilder.bind(testQueue).to(topicExchange).with(routingKey);
        amqpAdmin.declareQueue(testQueue);
        amqpAdmin.declareBinding(bind);
    }
}
