package com.wallstcn.transformation.map;

import com.wallstcn.common.ActionConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import org.apache.flink.api.common.functions.MapFunction;

public class FeaturesMapFuntion implements MapFunction<LogEntity,Void>{

    @Override
    public Void map(LogEntity logEntity) throws Exception {
        for  (Integer label : logEntity.getRelatedLabels()) {
            String key = Keys.getUserLabelActionScore(logEntity.getUserId(),label);
            RedisPool.get().incrByFloat(key, ActionConstant.FeaturesAction.HavePurchasedShortTermLabelPaymentColumnScore);
        }
        return null;
    }

    public static FeaturesMapFuntion create() {
        return new FeaturesMapFuntion();
    }
}
