package qiao.chat.route;

import qiao.chat.pojo.message.Message;

public interface MessageRoute {
    /**
     * 将消息转发到其他IMServer
     */
    void transferMessage(Message message);

    /**
     * 接收其他IMServer转发的请求。接收到转发时被回调。
     */
    void receiveMessage(Message message);
}
