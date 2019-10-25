package com.wallstcn.app;

import com.wallstcn.transformation.source.MysqlSource;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoMapFunction;

import java.util.HashMap;
import java.util.Map;

public class UserPortraitBroadCastTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(10, CheckpointingMode.EXACTLY_ONCE);
        DataStream<String> socketstream = env.socketTextStream("127.0.0.1",9000);
        DataStreamSource<Map<Integer,Double>> configStream = env.addSource(new MysqlSource("127.0.0.1",58666,"user_portrait","root","253Huaerjie!",10));
        DataStream<Map<Integer,Double>> broadcastStream = configStream.<Map<Integer,Double>>broadcast();
        socketstream.keyBy(0).connect(broadcastStream).map(new CoMapTest());
        env.execute("User broadcast");
    }

    public static class CoMapTest extends RichCoMapFunction<String, Map<Integer,Double>, Void> implements CheckpointedFunction {

        private ListState<Tuple2<Integer,Double>> listState;

        private Map<Integer,Double> snapShot = new HashMap<>();

        /**
         * 进行checkpoint进行快照
         * @param context
         * @throws Exception
         */
        @Override
        public void snapshotState(FunctionSnapshotContext context) throws Exception {
            System.out.println("1111");
            listState.clear();
            for(Map.Entry<Integer,Double> entry : snapShot.entrySet()){
                Integer action = entry.getKey();
                Double score = entry.getValue();
                listState.add(new Tuple2<>(action,score));
                System.out.println(action+":"+score);
            }
            System.out.println("2222");
        }

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
        public Void map1(String s) throws Exception {
            System.out.println("data:::"+s);
            System.out.println(snapShot);
            return null;
        }

        @Override
        public Void map2(Map<Integer, Double> integerDoubleMap) throws Exception {
            System.out.println("come in");
            for(Map.Entry<Integer,Double> entry : integerDoubleMap.entrySet()){
                snapShot.put(entry.getKey(),entry.getValue());
            }
            return null;
        }
    }
}
