package com.wallstcn.transformation;

import com.wallstcn.models.LogEntity;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class LogTimeStampPunctuateExtractor implements AssignerWithPunctuatedWatermarks<LogEntity> {

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(LogEntity logEntity, long extractedTimestamp) {
//        return logEntity.hasWatermarkMarker() ? new Watermark(extractedTimestamp) : null;;
        return null;
    }

    @Override
    public long extractTimestamp(LogEntity logEntity, long previousElementTimestamp) {
        return logEntity.getTimeStamp();
    }
}
