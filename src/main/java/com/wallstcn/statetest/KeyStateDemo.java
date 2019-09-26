package com.wallstcn.statetest;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

//import org.apache.flink.api.common.state.StateTtlConfig;

public class KeyStateDemo  extends RichFlatMapFunction<Tuple2<Long,Long>,Tuple2<Long,Long>>{

//    private Integer count = 0; //作用域为一个slot 跟operatorstate一样
//
//    private Map<Long,Integer> ha = new HashMap<>();
//
//    private ValueState<Tuple2<Long,Long>> valueState;

    @Override
    public void flatMap(Tuple2<Long, Long> value, Collector<Tuple2<Long, Long>> collector) throws Exception {
//        count++;
//        Tuple2<Long,Long> currentValue = valueState.value();
//        if (currentValue == null) {
//            currentValue = Tuple2.of(0L,0L);
//        }
//        currentValue.f0 += 1;
//        currentValue.f1 += value.f1;
//        valueState.update(currentValue);
//        if (currentValue.f0 >= 3) {
//            collector.collect(Tuple2.of(value.f0,currentValue.f1/currentValue.f0));
//            valueState.clear();
//        }
//        ha.put(value.f0,0);
//        System.out.println(count);
    }


    @Override
    public void open(Configuration parameters) throws Exception {
//        super.open(parameters);
//        //keyedState可以设置TTL过期时间
//        StateTtlConfig config = StateTtlConfig.newBuilder(Time.seconds(30)) //1
//                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired) //2
//                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite) //3
//                .build();
//        ValueStateDescriptor valueStateDescriptor = new ValueStateDescriptor("agvKeyedState", TypeInformation.of(new TypeHint<Tuple2<Long,Long>>() {}));
//        //设置支持TTL配置
//        valueStateDescriptor.enableTimeToLive(config);
//        valueState=getRuntimeContext().getState(valueStateDescriptor);
    }

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple2<Long,Long>> input=env.fromElements(
                Tuple2.of(1L,4L),
                Tuple2.of(1L,2L),
                Tuple2.of(1L,6L),
                Tuple2.of(2L,4L),
                Tuple2.of(2L,4L),
                Tuple2.of(3L,5L),
                Tuple2.of(2L,3L),
                Tuple2.of(1L,4L)
        );

        input.keyBy(0)
                .flatMap(new KeyStateDemo())
                .setParallelism(10)
                .print();

        env.execute();
    }
}

//TTL过期时间   对于每一个keyed State，还可以设置TTL过期时间，它会将过期的state删除掉，通过下面的方式来设置TTL：
// 1：ttl时长
// 3：定义状态的可见性，是否返回过期状态。启用 TTL：stateDescriptor.enableTimeToLive(retainOneDay);

//该newBuilder方法的第一个参数是必需的，它是生存时间值。
//
// 2 ：setUpdateType()：设置State更新类型，配置参数有：
//        StateTtlConfig.UpdateType.Disabled：状态不过期
//        StateTtlConfig.UpdateType.OnCreateAndWrite：仅仅是创建和写入权限（默认）
//        StateTtlConfig.UpdateType.OnReadAndWrite：读和写权限
//        setStateVisibility()：State可见状态，配置参数有：
//
//        StateTtlConfig.StateVisibility.NeverReturnExpired：过期值永不返回
//        StateTtlConfig.StateVisibility.ReturnExpiredIfNotCleanedUp：如果可用，则返回（没清除，还存在，返回）
