package com.wallstcn.transformation;

import com.wallstcn.models.LogEntity;
import org.apache.flink.api.common.functions.FilterFunction;

public class LogEntityFilterFunction implements FilterFunction<LogEntity> {
    @Override
    public boolean filter(LogEntity logEntity) throws Exception {
        return false;
    }
}
