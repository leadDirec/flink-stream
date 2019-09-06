package com.wallstcn.app;

import com.wallstcn.transformation.LogEntityFilterFunction;
import com.wallstcn.transformation.LogEntityMapFuntion;
import com.wallstcn.transformation.LogEntitytrigger;
import com.wallstcn.transformation.LogTimeStampPeriodExtractor;
import com.wallstcn.util.Property;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
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

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
        env.enableCheckpointing(1000, CheckpointingMode.EXACTLY_ONCE);

        env.setStateBackend(new RocksDBStateBackend(Property.getValue("state.checkpoints.dir"), true));

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
                .assignTimestampsAndWatermarks(new LogTimeStampPeriodExtractor()).setParallelism(parallelism)
                .keyBy("userId")
                .timeWindow(Time.days(6),Time.days(1))
                .trigger(LogEntitytrigger.create()).sum(1);
//                .evictor(new LogEntityEvictor())
//                .apply();
        env.execute("User Portrait");
    }
}