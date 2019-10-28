package com.wallstcn.transformation.map;

import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import com.wallstcn.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockCoMapFuntion extends BaseCoMapFuntion {

    private static final Logger logger = LoggerFactory.getLogger(StockCoMapFuntion.class);

    @Override
    public Void map1(LogEntity logEntity) throws Exception {
        logger.error("score StockCoMapFuntion >>>>>>>>>>>>"+ JacksonUtils.toJson(getConfig()));
        for (int i =0;i<logEntity.getRelatedLabels().length;i++) {
            Integer action = logEntity.getActions().get(i);
            Double score = getConfig().get(action);
            if (score == 0) {
                logger.error("score StockCoMapFuntion >>>>>>>>>>>> data  empty");
                return null;
            }
            String key = Keys.getUserLabelActionScore(logEntity.getUserId(),logEntity.getRelatedLabels()[i]);
            RedisPool.get().incrByFloat(key,score);
        }
        return null;
    }

    public static StockCoMapFuntion create() {
        return new StockCoMapFuntion();
    }
}
