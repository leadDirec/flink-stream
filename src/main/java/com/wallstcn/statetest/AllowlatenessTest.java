package com.wallstcn.statetest;

import com.wallstcn.common.CommonConstant;
import com.wallstcn.util.DateUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;

public class AllowlatenessTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<String> socketstream = env.socketTextStream("127.0.0.1",9000);
        SingleOutputStreamOperator operator = socketstream.map(new MapFunction<String, Tuple2<String,Long>>() {
            @Override
            public Tuple2<String, Long> map(String s) throws Exception {
                String[] data = s.split("\\W+");
                return new Tuple2<>(data[0], Long.parseLong(data[1]));
            }
        });
        operator.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple2<String,Long>>() {

            Long currentMaxTimestamp = 0L;
            Long maxOutOfOrderness = 3000L;

            @Nullable
            @Override
            public Watermark getCurrentWatermark() {
                return new Watermark(currentMaxTimestamp - maxOutOfOrderness);
            }

            @Override
            public long extractTimestamp(Tuple2<String,Long> element, long previousElementTimestamp) {
                Long timeStamp = element.f1;
                currentMaxTimestamp = Math.max(timeStamp,currentMaxTimestamp);
                return timeStamp;
            }
        });

        SingleOutputStreamOperator accumulatorWindow = operator.keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .allowedLateness(Time.seconds(5))
                .apply(new AccumulatingWindowFunction())
                .setParallelism(2);
        accumulatorWindow.print();
        env.execute("window accumulate test");
    }

    public static class AccumulatingWindowFunction extends RichWindowFunction<Tuple2<String,Long>,Tuple6<String, String, String, Integer, String, Integer>,String,TimeWindow> {

        private ValueState<Integer> valueState;

        Integer count = 0;

        @Override
        public void apply(String key, TimeWindow window, Iterable<Tuple2<String, Long>> input, Collector<Tuple6<String, String, String, Integer, String, Integer>> out) throws Exception {
            count = valueState.value() + 1;
            valueState.update(count);
            out.collect(new Tuple6<>(key, DateUtil.toDatebyStr(window.getStart(), CommonConstant.MS_TIME_PATTERN), DateUtil.toDatebyStr(window.getEnd(), CommonConstant.MS_TIME_PATTERN),count,DateUtil.toDatebyStr(System.currentTimeMillis(), CommonConstant.MS_TIME_PATTERN),count));
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            valueState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("AccumulatingWindow Test", TypeInformation.of(new TypeHint<Integer>() {})));
        }
    }
}
