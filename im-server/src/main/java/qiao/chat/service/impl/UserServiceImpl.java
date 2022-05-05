package qiao.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiao.chat.dao.UserMapper;
import qiao.chat.exception.BadRequest;
import qiao.chat.pojo.User;
import qiao.chat.pojo.request.AuthRequest;
import qiao.chat.service.UserService;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public boolean existsUserById(String userId) {
        return userMapper.existsUserById(userId);
    }

    @Override
    public User authenticate(AuthRequest authRequest) {
        User user = getUserByUsername(authRequest.getUsername());

        if(user == null || ! authRequest.getPassword().equals(user.getPassword())){
            throw new BadRequest("用户不存在或密码错误");
        }

        return user;
    }
}
