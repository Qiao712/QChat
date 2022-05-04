package qiao.chat.pojo.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageResponse extends Response{
    private String senderId;
    private String messageId;
    private Map<String, Object> messageBody;
}
