package com.wallstcn.transformation.filter;

import com.wallstcn.common.CommonConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import com.wallstcn.util.DateUtil;
import org.apache.flink.api.common.functions.FilterFunction;

import java.util.ArrayList;
import java.util.List;

public class ArticleFilterFunction implements FilterFunction<LogEntity> {

    @Override
    public boolean filter(LogEntity logEntity) throws Exception {
        String day = DateUtil.toDatebyStr(logEntity.getTimeStamp(), CommonConstant.DAY_PATTERN);
        if (logEntity.getRelatedLabels().length == 0) {
            return false;
        }
        List<Integer> labels = new ArrayList<>();
        for (Integer label : logEntity.getRelatedLabels()) {
            String key = Keys.getUserArticleActionKeys(logEntity.getUserId(),day,label);
            RedisPool.get().hincrBy(key, String.valueOf(logEntity.getAction()),1);
            Long ret = RedisPool.get().hincrBy(Keys.getUserLabelDatealKeys(logEntity.getUserId(),label),String.valueOf(logEntity.getAction()),1);
            if (ret == 1) { //当天第一次
                labels.add(label);
            }
        }
        if (!labels.isEmpty()) {
            int[] strings = new int[labels.size()];
            for(int i=0;i<labels.size();i++) {
                strings[i] = labels.get(i);
            }
            logEntity.setRelatedLabels(strings);
            return true;
        }
        return false;
    }

    public static ArticleFilterFunction create() {
        return new ArticleFilterFunction();
    }
}
