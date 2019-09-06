package com.wallstcn.transformation;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.Collection;

public class LogEntitySlidingEventTimeWindow extends WindowAssigner<Object, TimeWindow> {

    private static final long serialVersionUID = 1L;
    private final long size;
    private final long slide;
    private final long offset;

    protected LogEntitySlidingEventTimeWindow(long size, long slide, long offset) {
        if (offset >= 0L && offset < slide && size > 0L) {
            this.size = size;
            this.slide = slide;
            this.offset = offset;
        } else {
            throw new IllegalArgumentException("SlidingEventTimeWindows parameters must satisfy 0 <= offset < slide and size > 0");
        }
    }

    @Override
    public Collection<TimeWindow> assignWindows(Object o, long l, WindowAssignerContext windowAssignerContext) {
        return null;
    }

    @Override
    public Trigger<Object, TimeWindow> getDefaultTrigger(StreamExecutionEnvironment streamExecutionEnvironment) {
        return null;
    }

    @Override
    public TypeSerializer<TimeWindow> getWindowSerializer(ExecutionConfig executionConfig) {
        return null;
    }

    @Override
    public boolean isEventTime() {
        return false;
    }

    public static LogEntitySlidingEventTimeWindow of(Time size, Time slide) {
        return new LogEntitySlidingEventTimeWindow(size.toMilliseconds(), slide.toMilliseconds(), 0L);
    }
}
