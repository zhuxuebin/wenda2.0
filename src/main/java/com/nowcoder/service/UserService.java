package com.nowcoder.service;

import com.nowcoder.domain.User;
import com.nowcoder.domain.UserExample;

import java.util.Map;

/**
 * Created by xuery on 2016/8/22.
 */
public interface UserService {
    User getUser(int id);

    Map<String,Object> register(String username, String password);

    Map<String,Object> login(String username, String password);

    void logout(String ticket);

    User selectUserByOneExample(UserExample userExample);
}