package com.wallstcn.transformation.map;

import com.wallstcn.common.ActionConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import org.apache.flink.api.common.functions.MapFunction;

public class StockMapFuntion implements MapFunction<LogEntity,Void>{

    @Override
    public Void map(LogEntity logEntity) throws Exception {
        for (int i =0;i<logEntity.getRelatedLabels().length;i++) {
            Integer action = logEntity.getActions().get(i);
            Double score = 0.0;
            switch (action) {
                case ActionConstant.StockAction.BrowseStocksAction:
                    score = ActionConstant.StockAction.BrowseStocksActionScore;
                    break;
                case ActionConstant.StockAction.BrowseStocksFrequencyAction:
                    score = ActionConstant.StockAction.BrowseStocksFrequencyActionScore;
                    break;
                case ActionConstant.StockAction.SearchStocksAction:
                    score = ActionConstant.StockAction.SearchStocksActionScore;
                    break;
                default:
                    return null;
            }
            String key = Keys.getUserLabelActionScore(logEntity.getUserId(),logEntity.getRelatedLabels()[i]);
            RedisPool.get().incrByFloat(key,score);
        }
        return null;
    }

    public static StockMapFuntion create() {
        return new StockMapFuntion();
    }
}

