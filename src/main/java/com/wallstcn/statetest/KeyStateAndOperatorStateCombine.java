package com.wallstcn.statetest;

public class KeyStateAndOperatorStateCombine{
//public class KeyStateAndOperatorStateCombine extends FlatMapFunction<Map<Integer,Long>,Map<Integer,Map<Long,Long>>> implements CheckpointedFunction {
//
//    //定义算子实例本地变量，存储Operator数据数量
//    private Long operatorCount = null;
//    //定义keyedState，存储和key相关的状态值
//    private ValueState keyedState =null;
//    //定义operatorState,存储算子的状态值
//    private ListState operatorState = null;
//
//    @Override
//    public void flatMap(Map<Integer, Long> integerLongMap, Collector<Map<Integer, Map<Long, Long>>> collector) throws Exception {
//        long keyedCount okeyedState.value() +1;
////更新keyedState数量
//        keyedState.update(keyedCount);
////更新本地算子operatorCount值
//        operatorCount =operatorCount+1;
////输出结果，包括id,id对应的数量统计keyedCount,算子输入数据的数量统计operatorCount
//        collector.collect(t.f0,keyedCount,operatorCount);
//    }
//
//    @Override
//    public void snapshotState(FunctionSnapshotContext context) throws Exception {
//        operatorState.clear();
//        operatorState.add(operatorCount);
//    }
//
//    @Override
//    public void initializeState(FunctionInitializationContext context) throws Exception {
////定义并获取keyedState
//        ValueStateDescriptor KeyedDescriptor =new ValueStateDescriptor ("keyedState",createTypeInformation);
//        keyedState = context.getKeyedStateStore.getState(KeyedDescriptor );
////定义并获取operatorState
//        ValueStateDescriptor OperatorDescriptor =new ValueStateDescriptor ("OperatorState",createTypeInformation);
//        operatorState = context.getOperatorStateStore.getListState();
////定义在Restored过程中，从operatorState中回复数据的逻辑
//        if(context.isRestored){
//            operatorCount = operatorState.get()
//        }
}
