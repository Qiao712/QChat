package qiao.chat.util;

import qiao.chat.pojo.request.AuthRequest;
import qiao.chat.pojo.request.MessageRequest;
import qiao.chat.pojo.request.Request;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类型
 */
public class RequestTypes {
    private static Map<Integer, Class<? extends Request>> type2ClassMap = new ConcurrentHashMap<>();
    private static Map<Class<? extends Request>, Integer> class2TypeMap = new ConcurrentHashMap<>();

    //类型编号
    private static int type = 1;
    public static void registerRequestType(Class<? extends Request> cls){
        type2ClassMap.put(type, cls);
        class2TypeMap.put(cls, type);
        type++;
    }

    //注册类型
    static {
        registerRequestType(MessageRequest.class);  //1
        registerRequestType(AuthRequest.class);     //2
    }

    public static Class<? extends Request> getClassByType(int type){
        return type2ClassMap.get(type);
    }
}
