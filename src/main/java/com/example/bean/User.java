package com.example.bean;

import lombok.Data;

@Data
public class User {


    private int uid;
    private String uname;
    private String password;
    private int permission;

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", password='" + password + '\'' +
                ", permission=" + permission +
                '}';
    }
}
