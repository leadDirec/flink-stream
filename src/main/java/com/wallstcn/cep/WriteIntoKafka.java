package com.wallstcn.cep;

import com.wallstcn.cep.domain.AirQualityRecoder;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WriteIntoKafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Map prop = new HashMap();
        prop.put("bootstrap.servers", "localhost:9092");
        prop.put("topic", "test1");
        ParameterTool parameterTool = ParameterTool.fromMap(prop);
        DataStream<AirQualityRecoder> messageStream = env.addSource(new SimpleGenerator());
        DataStreamSink<AirQualityRecoder> airQualityVODataStreamSink = messageStream.addSink(new FlinkKafkaProducer010<>(parameterTool.getRequired("bootstrap.servers"),
                parameterTool.getRequired("topic"),
                new SimpleAirQualityRecoderSchema()));
        messageStream.print();
        env.execute("write to kafka !!!");
    }

    public static class SimpleGenerator implements SourceFunction<AirQualityRecoder> {

        private static final long serialVersionUID = 1L;
        boolean running = true;

        @Override
        public void run(SourceContext<AirQualityRecoder> ctx) throws Exception {
            while(running) {
                ctx.collect(AirQualityRecoder.createOne());
            }
        }

        @Override
        public void cancel() {
            running = false;
        }
    }

    public static class SimpleAirQualityRecoderSchema implements DeserializationSchema<AirQualityRecoder> , SerializationSchema<AirQualityRecoder> {

        @Override
        public AirQualityRecoder deserialize(byte[] message) throws IOException {
            ByteArrayInputStream bi = new ByteArrayInputStream(message);
            ObjectInputStream oi = new ObjectInputStream(bi);
            AirQualityRecoder obj = null;
            try {
                obj = (AirQualityRecoder)oi.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            bi.close();
            oi.close();
            return obj;
        }

        @Override
        public boolean isEndOfStream(AirQualityRecoder airQualityRecoder) {
            return false;
        }

        @Override
        public byte[] serialize(AirQualityRecoder element) {
            byte[] bytes = null;
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream oo = new ObjectOutputStream(bo);
                oo.writeObject(element);
                bytes = bo.toByteArray();
                bo.close();
                oo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bytes;
        }

        @Override
        public TypeInformation<AirQualityRecoder> getProducedType() {
            return TypeInformation.of(new TypeHint<AirQualityRecoder>(){});
        }
    }
}
