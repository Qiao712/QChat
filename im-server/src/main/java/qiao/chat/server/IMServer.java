package qiao.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qiao.chat.handler.AuthRequestHandler;
import qiao.chat.handler.ConnectStatusHandler;
import qiao.chat.handler.MessageRequestHandler;
import qiao.chat.handler.TextWebSocketFrameCodec;

@Component
public class IMServer implements InitializingBean{
    @Value("${chat.port}")
    private int port;
    private String websocketPath = "/ws";

    @Autowired
    AuthRequestHandler authRequestHandler;
    @Autowired
    MessageRequestHandler messageRequestHandler;
    @Autowired
    ConnectStatusHandler connectStatusHandler;

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline = socketChannel.pipeline();
                        channelPipeline.addLast(new HttpServerCodec());
                        channelPipeline.addLast(new HttpObjectAggregator(64*1024));
                        channelPipeline.addLast(new WebSocketServerProtocolHandler(websocketPath));
                        channelPipeline.addLast(new TextWebSocketFrameCodec());

                        channelPipeline.addLast(connectStatusHandler);
                        channelPipeline.addLast(authRequestHandler);
                        channelPipeline.addLast(messageRequestHandler);
                    }
                });

        ChannelFuture future = serverBootstrap.bind(port);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
