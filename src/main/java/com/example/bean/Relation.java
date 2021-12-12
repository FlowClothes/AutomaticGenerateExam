package com.example.bean;

import lombok.Data;

@Data
public class Relation {

    private int relationid;

    private String subjectid; //主

    private String predicateid; //谓语

    private String objectid; //宾1

    private String objectid2; //宾2

    private String objectid3; //宾3

    private String objectid4; //宾4

    private String objectid5; //宾5

    private String date;

    private String adverbial; //状语备用，现在看作文章id

}
