package qiao.chat.util;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * 用户连接上下文
 * 将业务从ChannelHandler中抽出后，使用ThreadLocal的ConnectionContext储存当前连接和当前用户。
 */
@Data
public class ConnectionContext {
    private String userId;
    private Channel channel;

    private static ThreadLocal<ConnectionContext> connectionContext = new ThreadLocal<>();

    /**
     * 获取当前连接的上下文
     * @return 返回当前的上下文。若无则返回null
     */
    public static ConnectionContext get(){
        return connectionContext.get();
    }

    /**
     * 设置上下文
     * @param context 当前连接的上下文
     */
    public static void set(ConnectionContext context){
        connectionContext.set(context);
    }
}
