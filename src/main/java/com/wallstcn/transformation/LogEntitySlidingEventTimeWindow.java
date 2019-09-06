package com.wallstcn.transformation;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import java.util.Collection;

/**
 * @author xiangdao
 */
@PublicEvolving
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
            throw new IllegalArgumentException("LogEntitySlidingEventTimeWindow parameters must satisfy 0 <= offset < slide and size > 0");
        }
    }

    @Override
    public Collection<TimeWindow> assignWindows(Object element, long timestamp, WindowAssignerContext context) {
        return null;
    }

    @Override
    public Trigger<Object, TimeWindow> getDefaultTrigger(StreamExecutionEnvironment streamExecutionEnvironment) {
        return EventTimeTrigger.create();
    }

    @Override
    public TypeSerializer<TimeWindow> getWindowSerializer(ExecutionConfig executionConfig) {
        return new TimeWindow.Serializer();
    }

    @Override
    public boolean isEventTime() {
        return true;
    }

    public static LogEntitySlidingEventTimeWindow of(Time size, Time slide) {
        return new LogEntitySlidingEventTimeWindow(size.toMilliseconds(), slide.toMilliseconds(), 0L);
    }

    public static LogEntitySlidingEventTimeWindow of(Time size, Time slide, Time offset) {
        return new LogEntitySlidingEventTimeWindow(size.toMilliseconds(), slide.toMilliseconds(), offset.toMilliseconds() % slide.toMilliseconds());
    }

    @Override
    public String toString() {
        return "LogEntitySlidingEventTimeWindow{" +
                "size=" + size +
                ", slide=" + slide +
                ", offset=" + offset +
                '}';
    }

    public long getSize() {
        return this.size;
    }

    public long getSlide() {
        return this.slide;
    }

}
