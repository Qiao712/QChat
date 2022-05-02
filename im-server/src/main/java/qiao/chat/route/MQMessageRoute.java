package qiao.chat.route;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import qiao.chat.constant.ProtocolConstant;
import qiao.chat.pojo.request.MessageRequest;
import qiao.chat.pojo.response.MessageResponse;
import qiao.chat.util.ConnectionManager;

import java.net.UnknownHostException;
import java.util.UUID;

@Component
public class MQMessageRoute implements MessageRoute, InitializingBean {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Value("${chat.id}")
    private String imServerId;

    @Autowired
    private ConnectionManager connectionManager;

    @Override
    public void transferMessageRequest(MessageRequest messageRequest) {
        String serverId = redisTemplate.opsForValue().get("user:" + messageRequest.getReceiverId());
        if(serverId != null){
            //根据serverAddress进行转发
            amqpTemplate.convertAndSend(ProtocolConstant.MESSAGE_EXCHANGE, "chat." + serverId, JSON.toJSONBytes(messageRequest));
        }
    }

    @Override
    public void receiveMessage(MessageRequest messageRequest){
        //推送消息至接收者客户端
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessageId(UUID.randomUUID().toString());
        messageResponse.setSenderId(messageResponse.getSenderId());
        messageResponse.setContent(messageRequest.getMessage());

        Channel receiverChannel = connectionManager.getChannelByUserId(messageRequest.getReceiverId());
        if(receiverChannel != null){
            receiverChannel.writeAndFlush(messageResponse);
        }else{
            System.out.println(messageRequest.getReceiverId() + "目标不在线");
        }
    }

    /**
     * 设置队列、交换机、监听器
     */
    public void setMessageListener() throws UnknownHostException {
        //声明队列交换机
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        DirectExchange exchange = new DirectExchange(ProtocolConstant.MESSAGE_EXCHANGE, false, true);
        Queue queue = new Queue("chat."+imServerId, false, false, true);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("chat."+imServerId);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);

        //绑定监听器
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(6);    //设置线程数
        container.addQueueNames("chat."+imServerId);
        container.setMessageListener(message -> {
            MessageRequest messageRequest = JSON.parseObject(message.getBody(), MessageRequest.class);
            receiveMessage(messageRequest);
        });

        container.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setMessageListener();
    }
}
