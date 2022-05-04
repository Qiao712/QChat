package qiao.chat.pojo.message;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Message {
    private String id;
    private String senderId;
    private String receiverId;
    private LocalDateTime sendTime;
    private Boolean acknowledged;

    private Integer type;
    private Map<String, Object> messageBody;
}
