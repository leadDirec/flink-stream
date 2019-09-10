package com.wallstcn.redis;

public class Keys {
    public static String UserActiveTimeLineKeys = "userportrait:timeline:%s"; //用户时间线 天（时间) list
    public static String UserArticleActionKeys = "userportrait:action:"; //用户行为 用户id 天（时间） 标签标识 hash
    public static String UserLabelKeys = "userportrait:label:%d"; //用户标签 用户id hash
    public static String UserLabelDatealKeys = "userportrait:labeldatail:%d"; //用户标签 用户id hash

    public static String getUserArticleActionKeys(Long userId,String day,Integer label) {
        StringBuilder sb = new StringBuilder(UserArticleActionKeys);
        return sb.append(day)
                .append(":")
                .append(userId)
                .append(":")
                .append(label).toString();
    }
}
