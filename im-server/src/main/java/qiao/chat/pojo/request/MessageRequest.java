package qiao.chat.pojo.request;

import lombok.Data;

@Data
public class MessageRequest extends Request {
    private String receiverId;
    private String message;
}
