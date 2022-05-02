package qiao.chat.pojo.request;

import lombok.Data;

@Data
public class AuthRequest extends Request {
    private String token;
}
