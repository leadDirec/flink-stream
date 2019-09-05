package com.wallstcn.app;

import com.wallstcn.util.Property;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class UserPortrait {

    private static final Logger logger = LoggerFactory.getLogger(UserPortrait.class);

    public static void main(String[] args) {
        Properties kfkPro = Property.getKafkaProperties();
        String[] topics = StringUtils.split(Property.getValue("kafka.group.topics"),",");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> transction = env.addSource(new FlinkKafkaConsumer010<String>(Arrays.asList(topics), new SimpleStringSchema(), kfkPro));
    }
}