package qiao.chat.pojo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Request {
    private Integer requestType;
    private LocalDateTime timestamp;
}
