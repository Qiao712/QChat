package qiao.chat;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMQ {
    @Autowired
    AmqpTemplate amqpTemplate;

    @Test
    public void test(){
        for(int i = 0; i<10; i++){
            amqpTemplate.convertAndSend("chat.exchange", "chat.test1", "测试测试");
        }
    }
}
