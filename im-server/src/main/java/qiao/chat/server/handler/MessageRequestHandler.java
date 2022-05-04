package qiao.chat.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiao.chat.pojo.request.MessageRequest;
import qiao.chat.pojo.response.MessageResponse;
import qiao.chat.route.MessageRoute;
import qiao.chat.service.MessageService;
import qiao.chat.util.ConnectionManager;

import java.util.UUID;

@Component
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequest> {
    @Autowired
    private MessageService messageService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest request) throws Exception {
        messageService.pushMessageToUser(request);
    }
}
