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
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPortraitTest {

    private static final Logger logger = LoggerFactory.getLogger(UserPortraitTest.class);

    public static void main(String[] args) throws Exception {

        final OutputTag<LogEntity> stocks = new OutputTag<LogEntity>(ActionConstant.StockAction.ActionType, TypeInformation.of(LogEntity.class));
        final OutputTag<LogEntity> articles = new OutputTag<LogEntity>(ActionConstant.ArticleAction.ActionType, TypeInformation.of(LogEntity.class));
        final OutputTag<LogEntity> features = new OutputTag<LogEntity>(ActionConstant.FeaturesAction.ActionType, TypeInformation.of(LogEntity.class));

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Integer parallelism = Property.getIntValue("kafka.consume.parallelism");

        DataStream<String> socketstream = env.socketTextStream("127.0.0.1",9000);
        SingleOutputStreamOperator<LogEntity> stream = socketstream
//        SingleOutputStreamOperator<LogEntity> stream = env.addSource(consumer).setParallelism(parallelism)
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
                .filter(StockFilterFunction.create()).setParallelism(parallelism)
                .map(StockMapFuntion.create()).setParallelism(parallelism);
        stream.getSideOutput(articles).keyBy("userId")
                .filter(ArticleFilterFunction.create()).setParallelism(parallelism)
                .map(ArticleMapFuntion.create()).setParallelism(parallelism);
        stream.getSideOutput(features).keyBy("userId")
                .filter(FeaturesFilterFunction.create()).setParallelism(parallelism)
                .map(FeaturesMapFuntion.create()).setParallelism(parallelism);

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