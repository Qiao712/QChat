package qiao.chat.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiao.chat.pojo.request.AuthRequest;
import qiao.chat.util.ConnectionManager;

@Component
@ChannelHandler.Sharable
public class AuthRequestHandler extends SimpleChannelInboundHandler<AuthRequest> {
    @Autowired
    ConnectionManager connectionManager;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AuthRequest request) throws Exception {
        System.out.println("登录:" + request.getToken());
        connectionManager.addChannel(request.getToken(), ctx.channel());
    }
}
