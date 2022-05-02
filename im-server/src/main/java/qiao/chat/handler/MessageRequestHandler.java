package qiao.chat.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiao.chat.pojo.request.MessageRequest;
import qiao.chat.pojo.response.MessageResponse;
import qiao.chat.route.MessageRoute;
import qiao.chat.util.ConnectionManager;

import java.util.UUID;

@Component
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequest> {
    @Autowired
    private ConnectionManager connectionManager;
    @Autowired
    private MessageRoute messageRoute;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest request) throws Exception {
        Channel receiverChannel = connectionManager.getChannelByUserId(request.getReceiverId());
        String senderId = connectionManager.getUserIdByChannel(ctx.channel());

        if(receiverChannel == null){
            //查询目标用户的channel是否位于其他服务器，转发至其他IMServer
            messageRoute.transferMessageRequest(request);
        }else{
            //推送消息至接收者客户端
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessageId(UUID.randomUUID().toString());
            messageResponse.setSenderId(senderId);
            messageResponse.setContent(request.getMessage());

            receiverChannel.writeAndFlush(messageResponse);
        }
    }
}
