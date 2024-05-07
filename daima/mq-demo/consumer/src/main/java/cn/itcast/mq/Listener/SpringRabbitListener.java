package cn.itcast.mq.Listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Component
public class SpringRabbitListener {
//@RabbitListener(queues = "simple.queue")
//    public void ListenSimpleQueue(String msg){
//    System.out.println("消费者接收到了simple.queue的消息：{"+msg+"}");
//}

    @RabbitListener(queues = "simple.queue")
    public void ListenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到了消息：{"+msg+"}"+ LocalDateTime.now());
        Thread.sleep(20);
    }
    @RabbitListener(queues = "simple.queue")
    public void ListenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2......接收到了消息：{"+msg+"}"+LocalDateTime.now());
        Thread.sleep(200);
    }
@RabbitListener(queues = "fanout.queue1")
    public void ListenFanoutQueue1(String msg){
    System.out.println("消费者接收到了fanout.queue1的消息：{"+msg+"}");
}
    @RabbitListener(queues = "fanout.queue2")
    public void ListenFanoutQueue2(String msg){
        System.out.println("消费者接收到了fanout.queue2的消息：{"+msg+"}");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "itcast.direct",type = ExchangeTypes.DIRECT),
            key = {"red","yellow"}
    ))
    public void ListenDirectQueue1(String msg){
        System.out.println("消费者接收到direct1的消息：{"+msg+"}");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "itcast.direct",type = ExchangeTypes.DIRECT),
            key = {"red","blue"}
    ))
    public void ListenDirectQueue2(String msg){
        System.out.println("消费者接收到direct2的消息：{"+msg+"}");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "topic.direct",type = ExchangeTypes.TOPIC),
            key = "chian.#"
    ))
    public void ListenTopicQueue1(String msg){
        System.out.println("消费者接收到topic.direct1的消息：{"+msg+"}");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "topic.direct",type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void ListenTopicQueue2(String msg){
        System.out.println("消费者接收到topic.direct2的消息：{"+msg+"}");
    }
    @RabbitListener(queues = "object.queue")
    public void ListenObjectQueue(Map<String,Object> msg){
        System.out.println("接收到object.queue的消息："+msg);
    }
}
