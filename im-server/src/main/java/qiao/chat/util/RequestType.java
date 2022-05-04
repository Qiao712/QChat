package qiao.chat.util;

import qiao.chat.pojo.request.AuthRequest;
import qiao.chat.pojo.request.MessageRequest;
import qiao.chat.pojo.request.Request;

/**
 * 请求类型
 */
public enum RequestType {
    MessageRequest(MessageRequest.class),
    AuthRequest(AuthRequest.class);

    private final Class<? extends Request> cls;

    RequestType(Class<? extends Request> cls){
        this.cls = cls;
    }

    public Class<? extends Request> getRequestClass(){
        return cls;
    }

    public static Class<? extends Request> getRequestClass(int typeNo){
        return values()[typeNo - 1].cls;
    }

    public static int getTypeNo(Class<? extends Request> cls){
        return RequestType.valueOf(cls.getSimpleName()).ordinal() + 1;
    }
}
