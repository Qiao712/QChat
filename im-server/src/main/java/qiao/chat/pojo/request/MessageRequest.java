package qiao.chat.pojo.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class MessageRequest extends Request {
    private String receiverId;
    private Integer messageType;
    private Map<String, Object> messageBody;
}
