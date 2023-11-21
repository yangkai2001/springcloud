package cn.itcast.mq.Listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SpringRabbitListener {
@RabbitListener(queues = "simple.queue")
    public void ListenSimpleQueue(String msg){
    System.out.println("消费者接收到了simple.queue的消息：{"+msg+"}");
}

}
