package qiao.chat.util;

import qiao.chat.pojo.message.MessageBody;
import qiao.chat.pojo.message.TextMessageBody;

public enum MessageType {
    TextMessage(TextMessageBody.class);

    private final Class<? extends MessageBody> cls;

    MessageType(Class<? extends MessageBody> cls){
        this.cls = cls;
    }

    public Class<? extends MessageBody> getMessageBodyClass(){
        return cls;
    }

    public static Class<? extends MessageBody> getMessageBodyClass(int typeNo){
        return values()[typeNo - 1].cls;
    }

    public static int getTypeNo(Class<? extends MessageBody> cls){
        return MessageType.valueOf(cls.getSimpleName()).ordinal() + 1;
    }
}
