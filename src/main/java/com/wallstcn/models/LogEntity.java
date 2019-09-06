package com.wallstcn.models;

public class LogEntity {
    Integer userId;
    Long timeStamp; //事件发生时间 毫秒

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static LogEntity getUserLog(String value) {
        String[] values = value.split(",");
        if (values.length < 1) {
            System.out.println("Message is not correct");
            return null;
        }
        LogEntity log = new LogEntity();
        log.setUserId(Integer.parseInt(values[0]));
        log.setTimeStamp(Long.parseLong(values[1]));
        return log;
    }

    @Override
    public String toString() {
        return "LogEntity{" +
                "userId=" + userId +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
