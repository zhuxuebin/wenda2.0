package com.nowcoder.domain;

import org.springframework.stereotype.Component;

/**
 * Created by xuery on 2016/8/23.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users= new ThreadLocal<User>();

    public User getUsers(){
        return users.get();
    }

    public void setUsers(User user){
        this.users.set(user);
    }

    public void  clear(){
        users.remove();
    }

}
