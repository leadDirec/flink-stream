package com.wallstcn.transformation;

import com.wallstcn.models.LogEntity;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

//CONTINUE:什么都不做。
//FIRE:触发计算。
//PURE:清除窗口的元素。
//FIRE_AND_PURE:触发计算和清除窗口元素

//参照内置触发器EventTimeTrigger的写法
public class LogEntitytrigger extends Trigger<LogEntity,TimeWindow> {

    private static final long serialVersionUID = 1L;

    @Override
    public TriggerResult onElement(LogEntity logEntity, long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        return TriggerResult.FIRE;
    }

    @Override
    public TriggerResult onProcessingTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        // CONTINUE是代表不做输出，也即是，此时我们想要实现比如100条输出一次，
        // 而不是窗口结束再输出就可以在这里实现。
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext triggerContext) throws Exception {
        return time == window.maxTimestamp() ? TriggerResult.FIRE : TriggerResult.CONTINUE;
    }

    @Override
    public void onMerge(TimeWindow window, OnMergeContext ctx) {
        // only register a timer if the time is not yet past the end of the merged window
        // this is in line with the logic in onElement(). If the time is past the end of
        // the window onElement() will fire and setting a timer here would fire the window twice.
        long windowMaxTimestamp = window.maxTimestamp();
        if (windowMaxTimestamp > ctx.getCurrentWatermark()) {
            ctx.registerEventTimeTimer(windowMaxTimestamp);
        }
    }

    @Override
    public void clear(TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
        triggerContext.deleteEventTimeTimer(timeWindow.maxTimestamp());
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    public String toString() {
        return "LogEntitytrigger()";
    }

    public static LogEntitytrigger create() {
        return new LogEntitytrigger();
    }
}


//Trigger接收两个泛型，一个是element类型，一个是窗口类型；
//它定义了onElement、onProcessingTime、onEventTime、canMerge、onMerge、clear几个方法，
// 其中onElement、onProcessingTime、onEventTime均需要返回TriggerResult
//onElement在每个element添加到window的时候会被回调；
// onProcessingTime在注册的event-time timer触发时会被回调；
// onEventTime在注册的processing-time timer触发时会被回调
// canMerge用于标识是否支持trigger state的合并，默认返回false；onMerge在多个window合并的时候会被触发；clear用于清除TriggerContext中存储的相关state
// Trigger还定义了TriggerContext及OnMergeContext；TriggerContext定义了注册及删除EventTimeTimer、ProcessingTimeTimer方法，同时还定义了getCurrentProcessingTime、getMetricGroup、getCurrentWatermark、getPartitionedState、getKeyValueState、getKeyValueState方法
// OnMergeContext继承了TriggerContext，它多定义了mergePartitionedState方法

//    每进入一个新数据，就会注册一遍这个回调函数。但是由于trigger会添加到set集合中，
//    因此重复添加相同的trigger不会真的注册重复的trigger。
//    详见：WindowOperator.registerEventTimeTimer() -> HeapInternalTimerService.registerEventTimeTimer -> InternalTimer.equals()