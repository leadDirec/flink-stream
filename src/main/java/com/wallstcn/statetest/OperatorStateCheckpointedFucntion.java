package com.wallstcn.statetest;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import scala.Tuple2;
import java.util.ArrayList;
import java.util.List;

//Operator State
//        Operator State是指在一个job中的一个task中的每一个operator对应着一个state，比如在一个job中，涉及到map，filter，sink等操作，
//        那么在这些operator中，每一个可以对应着一个state（一个冰并行度），如果是多个并行度，那么每一个并行度都对应着一个state。
//        对于Operator State主要有ListState可以进行使用
//        可以看见上述案例中我们实现了它的initializeState、snapshotState等方法，
//        如果实现了ListCheckpointed接口，就不需要我们自己初始化状态，
//        直接从之前的状态进行恢复，只需要实现以下两个方法即可：
//        List<T> snapshotState(long checkpointId, long timestamp) throws Exception;
//        void restoreState(List<T> state) throws Exception;

public class OperatorStateCheckpointedFucntion extends RichFlatMapFunction<Long, Tuple2<Integer,String>> implements CheckpointedFunction{

    //托管状态
    private ListState<Long> listState;
    //原始状态
    private List<Long> listElements;

    @Override
    public void flatMap(Long value, Collector<Tuple2<Integer, String>> collector) throws Exception {
        if (value == 1) {
            if (listElements.size() > 0 ) {
                StringBuffer sb = new StringBuffer();
                for (Long ele : listElements) {
                    sb.append(ele+" ");
                }
                int sum = listElements.size();
                collector.collect(new Tuple2<>(sum,sb.toString()));
                listElements.clear();
            }
        }  else {
            listElements.add(value);
        }
    }

    /**
     * 进行checkpoint进行快照
     * @param context
     * @throws Exception
     */
    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        listState.clear();
        for(Long ele:listElements){
            listState.add(ele);
        }
    }

    /**
     * state的初始状态，包括从故障恢复过来
     * @param context
     * @throws Exception
     */
    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor listStateDescriptor=new ListStateDescriptor("checkPointedList",
                TypeInformation.of(new TypeHint<Long>() {}));
        listState = context.getOperatorStateStore().getListState(listStateDescriptor);
        //如果是故障恢复
        if (context.isRestored()) {
            for (Long ele : listState.get()) {
                listElements.add(ele);
            }
            listState.clear();
        }
    }

    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        listElements=new ArrayList<Long>();
    }

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<Long> input=env.fromElements(1L,2L,3L,4L,7L,5L,1L,5L,4L,6L,1L,7L,8L,9L,1L);
        input.flatMap(new OperatorStateCheckpointedFucntion()).setParallelism(1).print();
        System.out.println(env.getExecutionPlan());
        env.execute();
    }
}
