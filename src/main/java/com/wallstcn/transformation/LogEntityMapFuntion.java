package com.wallstcn.transformation;

import com.wallstcn.models.LogEntity;
import org.apache.flink.api.common.functions.MapFunction;

public class LogEntityMapFuntion implements MapFunction<String,LogEntity>{
    @Override
    public LogEntity map(String s) throws Exception {
        LogEntity log = LogEntity.getUserLog(s);
        return log;
    }
}
