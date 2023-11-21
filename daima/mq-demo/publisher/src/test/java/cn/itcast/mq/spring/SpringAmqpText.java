package cn.itcast.mq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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


}
