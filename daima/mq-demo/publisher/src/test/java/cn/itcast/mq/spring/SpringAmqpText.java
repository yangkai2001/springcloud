package cn.itcast.mq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpText {
@Autowired
    private RabbitTemplate rabbitTemplate;
@Test
    public void testSendMessage2SimpleQueue(){
    String queueName="simple.queue";
    String message ="hellow,spring amqp!";
    rabbitTemplate.convertAndSend(queueName,message);

}
    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName="simple.queue";
        String message ="hellow,spring MQ!";
        for (int i = 0; i <=50 ; i++) {
            rabbitTemplate.convertAndSend(queueName,message+i);
        }
        Thread.sleep(20);

    }
    @Test
    public void testSendMessage2FanoutQueue(){
    //发给数据给交换机，让交换机将数据发给消费者
        String queueName="itcast.fanout";
        //要发送的消息
        String message ="hellow,spring amqp!";
        //发送的消息，参数分布为：交换机名称，RoutingKey（暂时为空），消息
        rabbitTemplate.convertAndSend(queueName," ",message);

    }
    @Test
    public void testSendMessage2DirectQueue(){
        //发给数据给交换机，让交换机将数据发给消费者
        String queueName="itcast.direct";
        //要发送的消息
        String message ="hellow,red!";
        //发送的消息，参数分布为：交换机名称，RoutingKey（暂时为空），消息
        rabbitTemplate.convertAndSend(queueName,"red",message);

    }
    @Test
    public void testSendMessage2TopicQueue(){
        //发给数据给交换机，让交换机将数据发给消费者
        String queueName="topic.direct";
        //要发送的消息
        String message ="中国的新闻";
        //发送的消息，参数分布为：交换机名称，RoutingKey（暂时为空），消息
        rabbitTemplate.convertAndSend(queueName,"chian.news",message);

    }

    @Test
    public void testSendObjectQueue(){
        Map<String,Object> msg=new HashMap<>();
        msg.put("name","荒");
        msg.put("age",18);
rabbitTemplate.convertAndSend("object.queue",msg);

}
}
