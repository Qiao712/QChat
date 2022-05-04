package qiao.chat.service.impl;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiao.chat.dao.MessageMapper;
import qiao.chat.exception.BadRequest;
import qiao.chat.pojo.message.Message;
import qiao.chat.pojo.request.AckMessageRequest;
import qiao.chat.pojo.request.MessageRequest;
import qiao.chat.pojo.response.MessageResponse;
import qiao.chat.route.MessageRoute;
import qiao.chat.service.MessageService;
import qiao.chat.util.ConnectionContext;
import qiao.chat.util.ConnectionManager;

import java.util.UUID;

@Component
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    MessageRoute messageRoute;
    @Autowired
    ConnectionManager connectionManager;

    @Override
    public void pushMessageToUser(MessageRequest messageRequest) {
        String senderId = ConnectionContext.get().getUserId();

        if(senderId == null){
            throw new BadRequest("连接未认证");
        }
        if(messageRequest.getReceiverId() == null){
            throw new BadRequest("接收者为null");
        }

        //MessageRequest -> Message
        Message message = new Message();
        message.setReceiverId(messageRequest.getReceiverId());
        message.setSenderId(senderId);
        message.setSendTime(messageRequest.getTimestamp());
        message.setAcknowledged(false);
        message.setType(messageRequest.getMessageType());
        message.setMessageBody(messageRequest.getMessageBody());     //JSON

        //持久化消息
        messageMapper.saveMessage(message);

        pushMessageToUser(message, true);
    }

    public void pushMessageToUser(Message message, boolean canTransfer){
        Channel receiverChannel = connectionManager.getChannelByUserId(message.getReceiverId());

        if(receiverChannel == null){
            if(canTransfer){
                //转发至其他IMServer
                messageRoute.transferMessage(message);
            }
        }else{
            //推送消息至接收者客户端
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessageId(UUID.randomUUID().toString());
            messageResponse.setSenderId(message.getSenderId());
            messageResponse.setMessageBody(message.getMessageBody());

            receiverChannel.writeAndFlush(messageResponse);
        }
    }

    @Override
    public void acknowledgeMessage(AckMessageRequest messageRequest) {

    }
}
