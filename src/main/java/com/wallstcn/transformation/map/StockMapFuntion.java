package com.wallstcn.transformation.map;

import com.wallstcn.common.ActionConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import org.apache.flink.api.common.functions.MapFunction;

public class StockMapFuntion implements MapFunction<LogEntity,Void>{

    @Override
    public Void map(LogEntity logEntity) throws Exception {
        Double score = 0.0;
        switch (logEntity.getAction()) {
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
        for  (Integer label : logEntity.getRelatedLabels()) {
            String key = Keys.getUserLabelDatealKeys(logEntity.getUserId(),label);
            RedisPool.get().hincrByFloat(key,String.valueOf(logEntity.getAction()),score);
        }
        return null;
    }

    public static StockMapFuntion create() {
        return new StockMapFuntion();
    }
}

