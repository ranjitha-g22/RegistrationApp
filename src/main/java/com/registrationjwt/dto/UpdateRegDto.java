package com.registrationjwt.dto;

public class UpdateRegDto {
    private String userName;
    private String emailId;
    private String password;
    private String mobile;

    public String getUserName() {
        return userName;
    }

    public void setUsername(String username) {
        userName = username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
