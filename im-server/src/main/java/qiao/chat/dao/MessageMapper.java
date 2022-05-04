package qiao.chat.dao;

import org.apache.ibatis.annotations.Mapper;
import qiao.chat.pojo.message.Message;

@Mapper
public interface MessageMapper {
    Message getMessageById(String id);
    boolean saveMessage(Message message);
    boolean deleteMessageById(String id);
    boolean updateMessageById(Message message);
}
