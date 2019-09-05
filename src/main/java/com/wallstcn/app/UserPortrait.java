package com.wallstcn.app;

import com.wallstcn.transformation.LogEntityFilterFunction;
import com.wallstcn.transformation.LogEntityMapFuntion;
import com.wallstcn.util.Property;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPortrait {

    private static final Logger logger = LoggerFactory.getLogger(UserPortrait.class);

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(1000, CheckpointingMode.AT_LEAST_ONCE);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        FlinkKafkaConsumer010<String> consumer = new FlinkKafkaConsumer010<>(Property.getKafkaTopics(), new SimpleStringSchema(), Property.getKafkaProperties());
//        consumer.setStartFromEarliest();     // start from the earliest record possible
//        consumer.setStartFromLatest();       // start from the latest record
//        consumer.setStartFromGroupOffsets(); // the default behaviour
//        Map<KafkaTopicPartition, Long> specificStartOffsets = new HashMap<>();
//        specificStartOffsets.put(new KafkaTopicPartition("myTopic", 0), 23L);
//        specificStartOffsets.put(new KafkaTopicPartition("myTopic", 1), 31L);
//        specificStartOffsets.put(new KafkaTopicPartition("myTopic", 2), 43L);

        Integer parallelism = Property.getIntValue("kafka.consume.parallelism");
        DataStream<String> transction = env.addSource(consumer).setParallelism(parallelism);
        transction.map(new LogEntityMapFuntion()).setParallelism(parallelism)
                .filter(new LogEntityFilterFunction()).setParallelism(parallelism)
                .keyBy(0)
                .timeWindow(Time.days(6),Time.days(1)).trigger().evictor().apply();
    }
}