package com.nowcoder.domain;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class Feed {
    private int id;

    private Date createdDate;

    private Integer userId;  //发表新鲜事的主体

    private String data;    //content

    private Integer type;   //EventType

    private JSONObject dataJSON = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}