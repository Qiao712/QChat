package qiao.chat.pojo.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class TextMessageBody implements MessageBody {
    private String content;
}
