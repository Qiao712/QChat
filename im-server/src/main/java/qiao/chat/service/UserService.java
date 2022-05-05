package qiao.chat.service;

import qiao.chat.pojo.User;
import qiao.chat.pojo.request.AuthRequest;

public interface UserService {
    User getUserByUsername(String username);

    boolean existsUserById(String userId);

    User authenticate(AuthRequest authRequest);
}
