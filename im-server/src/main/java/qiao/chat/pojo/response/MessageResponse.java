package qiao.chat.pojo.response;

import lombok.Data;

@Data
public class MessageResponse extends Response{
    private String senderId;
    private String messageId;
    private String content;
}
