package qiao.chat.pojo.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthRequest extends Request {
    private String username;
    private String password;
}
