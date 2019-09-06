package com.wallstcn.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class GsonUtil {

    private static Gson gson = null;
    static {
        gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    /**
     * 小写下划线的格式解析JSON字符串到对象
     * <p>例如 is_success->isSuccess</p>
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T fromJsonUnderScoreStyle(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * JSON字符串转为Map<String,String>
     * @param json
     * @return
     */
    @SuppressWarnings("all")
    public static <T> T fronJson2Map(String json) {
        return (T)gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }
    /**
     * 小写下划线的格式将对象转换成JSON字符�??
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return gson.toJson(src);
    }
    
    public static void main(String[] args) {
    }
}
