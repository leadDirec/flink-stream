package com.wallstcn.models;

public class LogEntity {
    Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public static LogEntity getUserLog(String value) {
        String[] values = value.split(",");
        if (values.length < 1) {
            System.out.println("Message is not correct");
            return null;
        }
        LogEntity log = new LogEntity();
        log.setUserId(Integer.parseInt(values[0]));
        return log;
    }
}
