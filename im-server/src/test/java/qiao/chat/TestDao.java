package qiao.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import qiao.chat.dao.MessageMapper;
import qiao.chat.pojo.message.Message;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestDao {
    @Autowired
    MessageMapper messageMapper;

    @Test
    public void insertMessage(){
        Message message = new Message();
        message.setSenderId("b");
        message.setReceiverId("a");
        message.setAcknowledged(false);
        message.setType(1);
        Map<String,Object> messageBody = new HashMap<>();
        messageBody.put("k", "v");
        message.setMessageBody(messageBody);
        messageMapper.saveMessage(message);
        System.out.println("生成主键后" + message);
    }

    @Test
    public void selectMessage(){
        Message message = messageMapper.getMessageById("8361b131cb8e11ecb08f0242ac110002");
        System.out.println(message);
        System.out.println(message.getMessageBody().get("k"));
    }
}
