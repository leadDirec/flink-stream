package com.wallstcn.app;

import com.wallstcn.util.Property;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPortrait {

    private static final Logger logger = LoggerFactory.getLogger(UserPortrait.class);

    public static void main(String[] args) {
        Property.getValue("ss");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    }
}
