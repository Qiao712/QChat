package qiao.chat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiao.chat.util.ConnectionManager;

/**
 * 管理连接状态
 */
@Component
@ChannelHandler.Sharable
public class ConnectStatusHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    ConnectionManager connectionManager;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //移除channel
        connectionManager.removeChannel(ctx.channel());
    }
}
