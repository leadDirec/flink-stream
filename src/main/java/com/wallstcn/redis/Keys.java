package com.wallstcn.redis;

public class Keys {
//    private static String UserActiveTimeLineKeys = "userportrait:timeline:%s"; //用户时间线 天（时间) list
    private static String UserArticleActionKeys = "userportrait:action:"; //用户行为 用户id 天（时间） 标签标识 hash
    private static String UserArticleActionArticleKeys = "userportrait:action:Article:"; //用户行为 用户id 天（时间） 标签标识 hash
    private static String UserArticleActionSrockKeys = "userportrait:action:stock:"; //用户行为 用户id 天（时间） 标签标识 hash
//    private static String UserLabelKeys = "userportrait:label:%d"; //用户标签 用户id hash
    private static String UserLabelDatealKeys = "userportrait:labeldatail:"; //用户标签 用户id hash 需要每天开始重算
    private static String UserLabelDatealArticleKeys = "userportrait:labeldatail:Article:"; //用户标签 用户id hash 需要每天开始重算
    private static String UserLabelDatealStockKeys = "userportrait:labeldatail:stock:"; //用户标签 用户id hash 需要每天开始重算
    private static String UserLabelActionScore = "userportrait:labelscore:"; //用户标签 用户id   存的是分数  需要每天开始重算

    public static String getUserArticleActionKeys(Long userId,String day,Integer label) {
        StringBuilder sb = new StringBuilder(UserArticleActionKeys);
        return sb.append(day)
                .append(":")
                .append(userId)
                .append(":")
                .append(label).toString();
    }

    public static String getUserArticleActionArticleKeys(Long userId,String day,Integer label) {
        StringBuilder sb = new StringBuilder(UserArticleActionArticleKeys);
        return sb.append(day)
                .append(":")
                .append(userId)
                .append(":")
                .append(label).toString();
    }

    public static String getUserArticleActionStockKeys(Long userId,String day,Integer label) {
        StringBuilder sb = new StringBuilder(UserArticleActionSrockKeys);
        return sb.append(day)
                .append(":")
                .append(userId)
                .append(":")
                .append(label).toString();
    }

    public static String getUserLabelDatealKeys(Long userId,Integer label) {
        StringBuilder sb = new StringBuilder(UserLabelDatealKeys);
        return sb.append(userId)
                .append(":")
                .append(label).toString();
    }

    public static String getUserLabelDatealArticleKeys(Long userId,Integer label) {
        StringBuilder sb = new StringBuilder(UserLabelDatealArticleKeys);
        return sb.append(userId)
                .append(":")
                .append(label).toString();
    }

    public static String getUserLabelDatealStockKeys(Long userId,Integer label) {
        StringBuilder sb = new StringBuilder(UserLabelDatealStockKeys);
        return sb.append(userId)
                .append(":")
                .append(label).toString();
    }

    public static String getUserLabelActionScore(Long userId,Integer label) {
        StringBuilder sb = new StringBuilder(UserLabelActionScore);
        return sb.append(userId)
                .append(":")
                .append(label).toString();
    }

}

