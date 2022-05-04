package qiao.chat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiao.chat.exception.BadRequest;
import qiao.chat.pojo.request.Request;
import qiao.chat.util.ConnectionContext;
import qiao.chat.util.ConnectionManager;

@Component
@ChannelHandler.Sharable
public class ConnectionContextHandler extends SimpleChannelInboundHandler<Request> {
    @Autowired
    ConnectionManager connectionManager;

    ConnectionContextHandler(){
        //关闭自动释放将消息放行
        super(false);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("registered context handler");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Request request) throws Exception {
        //阻止未认证的连接的请求
        String userId = connectionManager.getUserIdByChannel(context.channel());
        if(userId == null){
            throw new BadRequest("未认证");
        }

        //设置上下文
        ConnectionContext connectionContext = new ConnectionContext();
        connectionContext.setChannel(context.channel());
        connectionContext.setUserId(userId);
        ConnectionContext.set(connectionContext);

        //放行
        context.fireChannelRead(request);
    }
}
