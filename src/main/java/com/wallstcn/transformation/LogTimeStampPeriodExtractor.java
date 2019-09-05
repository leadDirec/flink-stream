package com.wallstcn.transformation;

import com.wallstcn.models.LogEntity;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class LogTimeStampPeriodExtractor implements AssignerWithPeriodicWatermarks<LogEntity> {

    private final long maxOutOfOrderness = 3500; // 3.5 seconds

    private long currentMaxTimestamp;


    @Override
    public long extractTimestamp(LogEntity logEntity, long previousElementTimestamp) {
        long timestamp =logEntity.getTimeStamp();
        currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp);
        return timestamp;
    }

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
//        return new Watermark(currentMaxTimestamp - maxOutOfOrderness);
        return null;
    }
}
