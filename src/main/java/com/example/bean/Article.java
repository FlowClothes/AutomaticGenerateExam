package com.example.bean;

import lombok.Data;

@Data
public class Article {

    private int article_id;
    private String articleName;
    private String uploader;
    private String uploadTime;
    private String articleType;
    private String articleAddress;
    private String articleState;
    private long articleSize;


}
