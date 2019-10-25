package com.wallstcn.transformation.map;

import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;

public class ArticleCoMapFuntion extends BaseCoMapFuntion {

    @Override
    public Void map1(LogEntity logEntity) throws Exception {
        Double score =  getConfig().get(logEntity.getAction());
        for  (Integer label : logEntity.getRelatedLabels()) {
            String key = Keys.getUserLabelActionScore(logEntity.getUserId(),label);
            RedisPool.get().incrByFloat(key,score);
        }
        return null;
    }

    public static ArticleCoMapFuntion create() {
        return new ArticleCoMapFuntion();
    }

}


