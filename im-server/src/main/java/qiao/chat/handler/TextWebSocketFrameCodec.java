package qiao.chat.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import qiao.chat.exception.BadRequest;
import qiao.chat.pojo.request.Request;
import qiao.chat.pojo.response.Response;
import qiao.chat.util.RequestTypes;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextWebSocketFrameCodec extends MessageToMessageCodec<TextWebSocketFrame, Response> {
    private static Pattern requestTypePattern;
    static{
        //匹配 "requestType" : 123
        requestTypePattern = Pattern.compile("\"requestType\"\\s*:\\s*(\\d+)");
    }

    /**
     * Response -> TextWebSocketFrame
     */
    @Override
    protected void encode(ChannelHandlerContext context, Response response, List<Object> list) throws Exception {
        //直接写String类型
        String responseJson = JSON.toJSONString(response);
        list.add(new TextWebSocketFrame(responseJson));

        System.out.println("[out]:" + responseJson);
    }

    /**
     * TextWebSocketFrame -> Response
     */
    @Override
    protected void decode(ChannelHandlerContext context, TextWebSocketFrame textFrame, List<Object> list) throws Exception {
        String json = textFrame.text();

        System.out.println("[in]:" + json);

        Matcher matcher = requestTypePattern.matcher(json);
        Class<? extends Request> cls = null;
        if(matcher.find()){
            Integer type = null;
            try{
                type = Integer.valueOf(matcher.group(1));
            }catch (NumberFormatException e){
                throw new BadRequest("请求格式错误", e);
            }

            cls = RequestTypes.getClassByType(type);
        }else{
            throw new BadRequest("请求格式错误");
        }

        if(cls == null){
            throw new BadRequest("请求格式错误");
        }

        Request request = JSON.parseObject(json, cls);

        context.fireChannelRead(request);
    }
}