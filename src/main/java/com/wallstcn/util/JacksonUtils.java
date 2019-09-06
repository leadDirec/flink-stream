package com.wallstcn.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;

public class JacksonUtils {
	
	private static final String DEFAULT_CHARSET_NAME = "UTF-8";
	
	private static ObjectMapper jackson = new ObjectMapper();
	
	static{
		jackson.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		jackson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jackson.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		jackson.setDateFormat(dateFormat); 
	}

	public static ObjectMapper getJackson() {
		return jackson;
	}

	public static void setJackson(ObjectMapper jackson) {
		JacksonUtils.jackson = jackson;
	}
	
	public static <T> T fromJson(String json, Class<T> clazz){
		try {
			return jackson.readValue(json,clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String toJson(Object obj){
		try {
			return jackson.writeValueAsString(obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T fromJsonToList(String json, Class<?> collectionClass, Class<?>... elementClasses) {
		try {
			JavaType javatype = jackson.getTypeFactory().constructParametricType(collectionClass, elementClasses);
			return jackson.readValue(json, javatype);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return jackson.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
	
	
	public static <T> String serialize(T object) {
        return JSON.toJSONString(object);
    }

    public static <T> T deserialize(String string, Class<T> clz) {
        return JSON.parseObject(string, clz);
    }

    public static <T> T load(Path path, Class<T> clz) throws IOException {
        return deserialize(
                new String(Files.readAllBytes(path), DEFAULT_CHARSET_NAME), clz);
    }

    public static <T> void save(Path path, T object) throws IOException {
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path,
                serialize(object).getBytes(DEFAULT_CHARSET_NAME),
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
	
}
