package com.wallstcn.transformation.map;

import com.wallstcn.models.LogEntity;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.RichCoMapFunction;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseCoMapFuntion extends RichCoMapFunction<LogEntity,Map<Integer,Double>,Void> implements CheckpointedFunction {

    private ListState<Tuple2<Integer,Double>> listState;

    private Map<Integer,Double> snapShot = new HashMap<>();

    public Map<Integer,Double> getConfig() {
        return snapShot;
    }

    /**
     * 进行checkpoint进行快照
     * @param context
     * @throws Exception
     */
    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        listState.clear();
        for(Map.Entry<Integer,Double> entry : snapShot.entrySet()){
            Integer action = entry.getKey();
            Double score = entry.getValue();
            listState.add(new Tuple2<>(action,score));
            System.out.println(action+":"+score);
        }
    }

    /**
     * state的初始状态，包括从故障恢复过来
     * @param context
     * @throws Exception
     */
    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor listStateDescriptor=new ListStateDescriptor("userportraitConfig",
                TypeInformation.of(new TypeHint<Tuple2<Integer,Double>>(){}));
        listState = context.getOperatorStateStore().getListState(listStateDescriptor);
        for (Tuple2<Integer,Double> ele : listState.get()) {
            snapShot.put(ele.f0,ele.f1);
        }
        listState.clear();
    }

    @Override
    public abstract Void map1(LogEntity logEntity) throws Exception;

    @Override
    public Void map2(Map<Integer, Double> integerDoubleMap) throws Exception {
        for(Map.Entry<Integer,Double> entry : integerDoubleMap.entrySet()){
            snapShot.put(entry.getKey(),entry.getValue());
        }
        return null;
    }
}
