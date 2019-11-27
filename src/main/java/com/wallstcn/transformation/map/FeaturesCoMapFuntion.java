package com.wallstcn.transformation.map;

import com.wallstcn.common.ActionConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeaturesCoMapFuntion extends BaseCoMapFuntion {

    private static final Logger logger = LoggerFactory.getLogger(FeaturesCoMapFuntion.class);

    @Override
    public Void map1(LogEntity logEntity) throws Exception {
//        logger.error("score FeaturesCoMapFuntion >>>>>>>>>>>>"+ JacksonUtils.toJson(getConfig()));
        Double score =  getConfig().get(logEntity.getAction());
        if (score == null || score == 0) {
            logger.error("score FeaturesCoMapFuntion >>>>>>>>>>>> data  empty");
            score =  ActionConstant.FeaturesAction.HavePurchasedShortTermLabelPaymentColumnScore;
        }
        for  (Integer label : logEntity.getRelatedLabels()) {
            String key = Keys.getUserLabelActionScore(logEntity.getUserId(),label);
            RedisPool.get().incrByFloat(key, score);
        }
        return null;
    }

    public static FeaturesCoMapFuntion create() {
        return new FeaturesCoMapFuntion();
    }
}
