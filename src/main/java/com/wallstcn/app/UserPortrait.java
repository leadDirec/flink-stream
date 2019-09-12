package com.wallstcn.app;

import com.wallstcn.common.ActionConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.transformation.LogEntityMapFuntion;
import com.wallstcn.transformation.filter.ArticleFilterFunction;
import com.wallstcn.transformation.filter.FeaturesFilterFunction;
import com.wallstcn.transformation.filter.StockFilterFunction;
import com.wallstcn.transformation.map.ArticleMapFuntion;
import com.wallstcn.transformation.map.FeaturesMapFuntion;
import com.wallstcn.transformation.map.StockMapFuntion;
import com.wallstcn.util.Property;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPortrait {

    private static final Logger logger = LoggerFactory.getLogger(UserPortrait.class);

//    private static final OutputTag<LogEntity> ShortTermUsersPunchBoard = new OutputTag<>("ShortTermUsersPunchBoard");
//    private static final OutputTag<LogEntity> ShortTermUsersSubjectMatter = new OutputTag<>("ShortTermUsersSubjectMatter");
//    private static final OutputTag<LogEntity> MidlineUsersIndustry = new OutputTag<>("MidlineUsersIndustry");
//    private static final OutputTag<LogEntity> LongTermUsersCompanies = new OutputTag<>("LongTermUsersCompanies");
//    private static final OutputTag<LogEntity> EnthusiastsTechnophile = new OutputTag<>("EnthusiastsTechnophile");
//    private static final OutputTag<LogEntity> SmallWhiteUsers = new OutputTag<>("SmallWhiteUsers");

    private static final OutputTag<LogEntity> stocks = new OutputTag<>(ActionConstant.StockAction.ActionType, TypeInformation.of(LogEntity.class));
    private static final OutputTag<LogEntity> articles = new OutputTag<>(ActionConstant.ArticleAction.ActionType, TypeInformation.of(LogEntity.class));
    private static final OutputTag<LogEntity> features = new OutputTag<>(ActionConstant.FeaturesAction.ActionType, TypeInformation.of(LogEntity.class));

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        // 每隔1000 ms进行启动一个检查点【设置checkpoint的周期】
//        env.enableCheckpointing(1000);
//// 高级选项：
//// 设置模式为exactly-once （这是默认值）
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//// 确保检查点之间有至少500 ms的间隔【checkpoint最小间隔】
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
//// 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
//        env.getCheckpointConfig().setCheckpointTimeout(60000);
//// 同一时间只允许进行一个检查点
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//// 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint【详细解释见备注】
//        env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION:表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint
//        ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION: 表示一旦Flink处理程序被cancel后，会删除Checkpoint数据，只有job执行失败的时候才会保存checkpoint


//        env.setParallelism(1);
        env.enableCheckpointing(1000, CheckpointingMode.EXACTLY_ONCE);

//        env.setStateBackend(new RocksDBStateBackend(Property.getValue("state.checkpoints.dir"), true).getCheckpointBackend());

        env.setStateBackend(new FsStateBackend(Property.getValue("state.checkpoints.dir"),true));
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
        //TODO 细化 多次分流
//        SingleOutputStreamOperator<LogEntity> labelStream = env.addSource(consumer).setParallelism(parallelism)
//                .map(new LogEntityMapFuntion()).setParallelism(parallelism)
//                .process(new ProcessFunction<LogEntity, LogEntity>() {
//                    @Override
//                    public void processElement(LogEntity value, Context ctx, Collector<LogEntity> out) throws Exception {
//                        for (Integer label : value.getRelatedLabels()) {
//                            if (label == ActionConstant.UserLabel.ShortTermUsersPunchBoard) {
//                                ctx.output(ShortTermUsersPunchBoard, value);
//                            }
//                            if (label == ActionConstant.UserLabel.ShortTermUsersSubjectMatter) {
//                                ctx.output(ShortTermUsersSubjectMatter, value);
//                            }
//                            if (label == ActionConstant.UserLabel.MidlineUsersIndustry) {
//                                ctx.output(MidlineUsersIndustry, value);
//                            }
//                            if (label == ActionConstant.UserLabel.LongTermUsersCompanies) {
//                                ctx.output(LongTermUsersCompanies, value);
//                            }
//                            if (label == ActionConstant.UserLabel.EnthusiastsTechnophile) {
//                                ctx.output(EnthusiastsTechnophile, value);
//                            }
//                            if (label == ActionConstant.UserLabel.SmallWhiteUsers) {
//                                ctx.output(SmallWhiteUsers, value);
//                            }
//                        }
//                    }
//                }).setParallelism(parallelism);
//
//        SingleOutputStreamOperator<LogEntity> ShortTermUsersPunchBoardStream = labelStream.getSideOutput(ShortTermUsersPunchBoard).process(new ProcessFunction<LogEntity, LogEntity>() {
//            @Override
//            public void processElement(LogEntity value, Context ctx, Collector<LogEntity> out) throws Exception {
//                UserPortrait.split(value,ctx);
//            }
//        }).setParallelism(parallelism);

        SingleOutputStreamOperator<LogEntity> stream = env.addSource(consumer).setParallelism(parallelism)
                .map(new LogEntityMapFuntion()).setParallelism(parallelism)
                .process(new ProcessFunction<LogEntity, LogEntity>() {
                    @Override
                    public void processElement(LogEntity value, Context ctx, Collector<LogEntity> out) throws Exception {
                        if (ActionConstant.StockAction.ActionType.equals(value.getActionType())) {
                            ctx.output(stocks, value);
                        } else if (ActionConstant.ArticleAction.ActionType.equals(value.getActionType())) {
                            ctx.output(articles, value);
                        } else if (ActionConstant.FeaturesAction.ActionType.equals(value.getActionType())) {
                            ctx.output(features, value);
                        }
                    }
                }).setParallelism(parallelism);

        stream.getSideOutput(stocks).keyBy("userId")
                .filter(StockFilterFunction.create()).setParallelism(5)
                .map(StockMapFuntion.create()).setParallelism(5);
        stream.getSideOutput(articles).keyBy("userId")
                .filter(ArticleFilterFunction.create()).setParallelism(5)
                .map(ArticleMapFuntion.create()).setParallelism(5);
        stream.getSideOutput(features).keyBy("userId")
                .filter(FeaturesFilterFunction.create()).setParallelism(5)
                .map(FeaturesMapFuntion.create()).setParallelism(5);

//        DataStream<String> transction = env.addSource(consumer).setParallelism(parallelism);
//        transction.map(new LogEntityMapFuntion()).setParallelism(parallelism)
//                .filter(new LogEntityFilterFunction()).setParallelism(parallelism)
//                .assignTimestampsAndWatermarks(new LogTimeStampPeriodExtractor()).setParallelism(parallelism)
//                .keyBy("userId")
//                .window(LogEntitySlidingEventTimeWindow.of(Time.days(5),Time.days(1)))
//                .trigger(LogEntitytrigger.create())
////                .evictor(new LogEntityEvictor())
//                .reduce((logEntity, t1) -> {
//                    LogEntity entity = new LogEntity();
//                    entity.setUserId(t1.getUserId());
//                    entity.setTimeStamp(t1.getTimeStamp());
//                    return entity;
//                });
        env.execute("User Portrait");
    }

//    public static void split(LogEntity value, ProcessFunction.Context ctx) {
//        if (ActionConstant.StockAction.ActionType.equals(value.getActionType())) {
//            ctx.output(stocks, value);
//        } else if (ActionConstant.ArticleAction.ActionType.equals(value.getActionType())) {
//            ctx.output(articles, value);
//        } else if (ActionConstant.FeaturesAction.ActionType.equals(value.getActionType())) {
//            ctx.output(features, value);
//        }
//    }
}