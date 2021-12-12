package com.example.dao;

import com.example.bean.User;

public interface UserDao {

    User login(User user);

    User search(String uname);

    void insert(User user);

}
