package qiao.chat.util;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理用户的连接
 */
@Component
public class ConnectionManager {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${chat.id}")
    private String imServerId;

    Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    Map<Channel, String> channelUserMap = new ConcurrentHashMap<>();

    /**
     * 通过用户id获取连接
     * @param userId
     * @return channel
     */
    public Channel getChannelByUserId(String userId){
        return userChannelMap.get(userId);
    }

    /**
     * 通过channel获取用户id
     * @param channel
     * @return 用户id
     */
    public String getUserIdByChannel(Channel channel){
        return channelUserMap.get(channel);
    }

    /**
     * 记录连接
     * @param userId
     * @param channel
     */
    public void addChannel(String userId, Channel channel){
        userChannelMap.put(userId, channel);
        channelUserMap.put(channel, userId);

        //在redis中记录连接信息 userId -> 服务器标识
        redisTemplate.opsForValue().set("user:" + userId, imServerId);
    }

    /**
     * 移除连接
     * @param
     * @return 被移除的连接
     */
    public Channel removeChannel(Channel channel){
        String userId = channelUserMap.remove(channel);
        if(userId != null){
            //在redis移除连接信息
            redisTemplate.delete("user:" + userId);

            return userChannelMap.remove(userId);
        }

        return null;
    }
}
