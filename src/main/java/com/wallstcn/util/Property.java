package com.wallstcn.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Property {

    private static final String envKey = "CONFIGOR_ENV";

    private static final Logger logger = LoggerFactory.getLogger(Property.class);

    private final static String CONF_NAME = ".config.properties";

    private static Properties contextProperties;

    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(Property.getEnvConf(""));
        contextProperties = new Properties();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            contextProperties.load(inputStreamReader);
        } catch (IOException e) {
            logger.debug(">>>userportrait<<<资源文件加载失败!");
            e.printStackTrace();
        }
        logger.debug(">>>userportrait<<<资源文件加载成功");
    }

//    public static String getEnvConf(String jvmKey, String key) {
//        String jvm = System.getProperty(jvmKey);
//        String env = System.getProperty(key);
//        return StringUtils.isNotEmpty(jvm) ? jvm : //
//                (StringUtils.isNotEmpty(env) ? "classpath:config/spring/" + env
//                        + "-env-conf.properties" : //
//                        "classpath:config/spring/dev-env-conf.properties");
//    }

    public static String getEnvConf(String key) {
        if (StringUtils.isEmpty(key)) {
            key = envKey;
        }
        String env = System.getProperty(key);
        if (StringUtils.isEmpty(env)) {
            return "local".concat(CONF_NAME);
        }
        return env.concat(CONF_NAME);
    }

    public static String getValue(String key) {
        return contextProperties.getProperty(key);
    }

    public static int getIntValue(String key) {
        String strValue = getValue(key);
        // 注意，此处没有做校验，暂且认为不会出错
        return Integer.parseInt(strValue);
    }

    public static Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", getValue("kafka.bootstrap.servers"));
        properties.setProperty("zookeeper.connect", getValue("kafka.zookeeper.connect"));
        properties.setProperty("group.id", getValue("kafka.group.id"));
        return properties;
    }

}