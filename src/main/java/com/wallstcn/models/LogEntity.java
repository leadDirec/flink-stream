package com.wallstcn.models;

import java.util.Arrays;
import java.util.function.ToIntFunction;

public class LogEntity {
    public Long userId;
    public Long timeStamp; //事件发生时间 毫秒
    public String actionType;
    public Integer Action;
    public int[] relatedLabels;

    public int[] getRelatedLabels() {
        return relatedLabels;
    }

    public void setRelatedLabels(int[] relatedLabels) {
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
        log.setActionType(values[2]);
        log.setAction(Integer.parseInt(values[3]));
        if (values.length > 4) {
            int[] labels = Arrays.stream(values).skip(4).mapToInt(new ToIntFunction<String>() {
                @Override
                public int applyAsInt(String v) {
                    return Integer.parseInt(v);
                }
            }).toArray();
            log.setRelatedLabels(labels);
        }
        return log;
    }

    public static void main(String[] args) {
        String[] values = new String[10];
        values[0] = "111";
        values[1] = "111";
        values[2] = "111";
        values[3] = "111";
        values[4] = "111";
        values[5] = "111";
        values[6] = "111";
        values[7] = "111";
        values[8] = "111";
        values[9] = "111";
        String a = String.join(",",values);
        LogEntity la = LogEntity.getUserLog(a);
        for (Integer l  : la.getRelatedLabels() ) {
            System.out.println(l);
        }
    }

}
