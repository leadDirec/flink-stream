package com.wallstcn.transformation.filter;

import com.wallstcn.common.CommonConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import com.wallstcn.util.DateUtil;
import org.apache.flink.api.common.functions.FilterFunction;

public class FeaturesFilterFunction implements FilterFunction<LogEntity> {

    @Override
    public boolean filter(LogEntity logEntity) throws Exception {
        String day = DateUtil.toDatebyStr(System.currentTimeMillis(), CommonConstant.DAY_PATTERN);
        if (logEntity.getRelatedLabels().size() == 0) {
            return false;
        }
        for (Integer label : logEntity.getRelatedLabels()) {
            String key = Keys.getUserArticleActionKeys(logEntity.getUserId(),day,label);
            Long ret = RedisPool.get().hincrBy(key, String.valueOf(logEntity.getAction()),1);
            if (ret == 1) { //当天第一次
                return true;
            }
        }
        return false;
    }

    public static FeaturesFilterFunction create() {
        return new FeaturesFilterFunction();
    }
}
