package com.wallstcn.transformation;

import com.wallstcn.models.LogEntity;
import org.apache.flink.streaming.api.windowing.evictors.Evictor;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.runtime.operators.windowing.TimestampedValue;

public class LogEntityEvictor implements Evictor<LogEntity,TimeWindow>{
    @Override
    public void evictBefore(Iterable<TimestampedValue<LogEntity>> iterable, int i, TimeWindow timeWindow, EvictorContext evictorContext) {

    }

    @Override
    public void evictAfter(Iterable<TimestampedValue<LogEntity>> iterable, int i, TimeWindow timeWindow, EvictorContext evictorContext) {

    }
}
