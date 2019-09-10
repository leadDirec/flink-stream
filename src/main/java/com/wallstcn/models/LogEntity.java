package com.wallstcn.models;

import java.util.List;

public class LogEntity {
    public Long userId;
    public Long timeStamp; //事件发生时间 毫秒
    public String actionType;
    public Integer Action;
    public List<Integer> relatedLabels;

    public List<Integer> getRelatedLabels() {
        return relatedLabels;
    }

    public void setRelatedLabels(List<Integer> relatedLabels) {
        this.relatedLabels = relatedLabels;
    }

    @Override
    public String toString() {
        return "LogEntity{" +
                "userId=" + userId +
                ", timeStamp=" + timeStamp +
                ", actionType='" + actionType + '\'' +
                ", Action=" + Action +
                ", relatedLabels=" + relatedLabels +
                '}';
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getAction() {
        return Action;
    }

    public void setAction(Integer action) {
        Action = action;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
        log.setUserId(Long.parseLong(values[0]));
        log.setTimeStamp(Long.parseLong(values[1]));
        return log;
    }

}
