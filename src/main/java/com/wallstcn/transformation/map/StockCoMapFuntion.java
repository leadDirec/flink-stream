package com.wallstcn.transformation.map;

import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;

public class StockCoMapFuntion extends BaseCoMapFuntion {

    @Override
    public Void map1(LogEntity logEntity) throws Exception {
        for (int i =0;i<logEntity.getRelatedLabels().length;i++) {
            Integer action = logEntity.getActions().get(i);
            Double score = getConfig().get(action);
            String key = Keys.getUserLabelActionScore(logEntity.getUserId(),logEntity.getRelatedLabels()[i]);
            RedisPool.get().incrByFloat(key,score);
        }
        return null;
    }

    public static StockCoMapFuntion create() {
        return new StockCoMapFuntion();
    }
}

