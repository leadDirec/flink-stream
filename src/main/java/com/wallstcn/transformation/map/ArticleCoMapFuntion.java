package com.wallstcn.transformation.map;

import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import com.wallstcn.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleCoMapFuntion extends BaseCoMapFuntion {

    private static final Logger logger = LoggerFactory.getLogger(ArticleCoMapFuntion.class);

    @Override
    public Void map1(LogEntity logEntity) throws Exception {
        logger.error("score ArticleCoMapFuntion >>>>>>>>>>>>"+JacksonUtils.toJson(getConfig()));
        Double score =  getConfig().get(logEntity.getAction());
        if (score == 0) {
            logger.error("score ArticleCoMapFuntion >>>>>>>>>>>> data  empty");
            return null;
        }
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

