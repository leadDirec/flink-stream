package com.wallstcn.transformation.filter;

import com.wallstcn.common.ActionConstant;
import com.wallstcn.common.CommonConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import com.wallstcn.util.DateUtil;
import org.apache.flink.api.common.functions.FilterFunction;

import java.util.ArrayList;
import java.util.List;

public class StockFilterFunction implements FilterFunction<LogEntity> {

    @Override
    public boolean filter(LogEntity logEntity) throws Exception {
        String day = DateUtil.toDatebyStr(logEntity.getTimeStamp(), CommonConstant.DAY_PATTERN);
        if (logEntity.getRelatedLabels().length == 0) {
            return false;
        }
        List<Integer> labels = new ArrayList<>();
        List<Integer> actions = new ArrayList<>();
        for (Integer label : logEntity.getRelatedLabels()) {
            String key = Keys.getUserArticleActionKeys(logEntity.getUserId(),day,label);
            RedisPool.get().hincrBy(key, String.valueOf(logEntity.getAction()),1);
            Long ret = RedisPool.get().hincrBy( Keys.getUserLabelDatealKeys(logEntity.getUserId(),label),String.valueOf(logEntity.getAction()),1);
            if (logEntity.getAction() == ActionConstant.StockAction.BrowseStocksAction) {
                if (ret == 1) {
                    actions.add(ActionConstant.StockAction.BrowseStocksAction);
                    labels.add(label);
                }
                if (ret.equals(ActionConstant.StockAction.BrowseStocksFrequencyCount)) {
                    actions.add(ActionConstant.StockAction.BrowseStocksFrequencyAction);
                    labels.add(label);
                }
            } else {
//                if (!RedisPool.get().getbit(Keys.getUserActionDuplicateKeys(logEntity.getUserId(),label),logEntity.getAction() % 3000)) {
//                    RedisPool.get().setbit(Keys.getUserActionDuplicateKeys(logEntity.getUserId(),label),logEntity.getAction() % 3000,true);
//                    labels.add(label); //5天一次
//                }
                if (ret == 1) {
                    labels.add(label);
                }
            }
        }
        if (!labels.isEmpty()) {
            int[] strings = new int[labels.size()];
            for(int i=0;i<labels.size();i++) {
                strings[i] = labels.get(i);
            }
            logEntity.setActions(actions);
            logEntity.setRelatedLabels(strings);
            return true;
        }
        return false;
    }

    public static StockFilterFunction create() {
        return new StockFilterFunction();
    }

}
