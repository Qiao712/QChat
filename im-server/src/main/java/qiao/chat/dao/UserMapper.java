package qiao.chat.dao;

import org.apache.ibatis.annotations.Mapper;
import qiao.chat.pojo.User;

@Mapper
public interface UserMapper {
    User getUserByUsername(String username);

    boolean existsUserById(String id);
}
