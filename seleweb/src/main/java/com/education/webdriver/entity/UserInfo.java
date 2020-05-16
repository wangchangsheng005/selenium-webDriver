package com.education.webdriver.entity;

/**
 * @ClassName:UserInfo
 * @Description:TODO
 * @Author:SH-WANGCS2
 * @Date:2020/5/15/0015 23:13
 * @Version 1.0
 **/
public class UserInfo {

    private String userId;

    private String password;

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
