package qiao.chat.route;

import qiao.chat.pojo.request.MessageRequest;

public interface MessageRoute {
    /**
     * 将请求转发到其他IMServer
     */
    void transferMessageRequest(MessageRequest messageRequest);

    /**
     * 接收其他IMServer转发的请求。接收到转发时被回调。
     */
    void receiveMessage(MessageRequest messageRequest);
}
