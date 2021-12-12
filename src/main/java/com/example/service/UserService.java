package com.example.service;

import com.example.bean.User;

public interface UserService {

    User login(User user);

    User search(String uname);

    void insert(User user);

}
