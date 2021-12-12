package com.example.service.impl;

import com.example.bean.User;
import com.example.dao.UserDao;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(User user) {
        User login = userDao.login(user);
        if (login != null){
            return login;
        }
        throw new RuntimeException("登录失败");
    }

    @Override
    public User search(String uname) {
        User user = userDao.search(uname);
        return user;
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }
}
