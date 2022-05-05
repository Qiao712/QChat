package qiao.chat.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiao.chat.pojo.User;
import qiao.chat.pojo.request.AuthRequest;
import qiao.chat.service.UserService;
import qiao.chat.util.ConnectionManager;

@Component
@ChannelHandler.Sharable
public class AuthRequestHandler extends SimpleChannelInboundHandler<AuthRequest> {
    @Autowired
    private UserService userService;
    @Autowired
    private ConnectionManager connectionManager;

    @Override
    protected void channelRead0(ChannelHandlerContext context, AuthRequest request) throws Exception {
        //认证并绑定用户与通道
        User user = userService.authenticate(request);
        if(user != null){
            //TODO: 跨服务器的强制下线
            //关闭旧通道
            Channel oldChannel = connectionManager.getChannelByUserId(user.getId());
            if(oldChannel != null){
                oldChannel.close();
            }

            connectionManager.addChannel(user.getId(), context.channel());
        }
    }
}
