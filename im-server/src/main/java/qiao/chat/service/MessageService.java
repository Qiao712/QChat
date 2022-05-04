package qiao.chat.service;

import qiao.chat.pojo.message.Message;
import qiao.chat.pojo.request.AckMessageRequest;
import qiao.chat.pojo.request.MessageRequest;

public interface MessageService {
    /**
     * 将消推送给指定用户
     * @param messageRequest
     */
    void pushMessageToUser(MessageRequest messageRequest);

    /**
     * 将消推送给指定用户
     * @param message
     * @param canTransfer 是否可以向其他IMServer转发
     */
    void pushMessageToUser(Message message, boolean canTransfer);

    /**
     * 确认消息
     */
    void acknowledgeMessage(AckMessageRequest messageRequest);
}
