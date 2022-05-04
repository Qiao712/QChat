package qiao.chat.pojo.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AckMessageRequest extends Request{
    private String messageId;
}
