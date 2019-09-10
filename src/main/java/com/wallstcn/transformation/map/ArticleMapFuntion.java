package com.wallstcn.transformation.map;

import com.wallstcn.common.ActionConstant;
import com.wallstcn.models.LogEntity;
import com.wallstcn.redis.Keys;
import com.wallstcn.redis.RedisPool;
import org.apache.flink.api.common.functions.MapFunction;

public class ArticleMapFuntion implements MapFunction<LogEntity,Void>{

    @Override
    public Void map(LogEntity logEntity) throws Exception {
        Double score = 0.0;
        switch (logEntity.getAction()) {
            case ActionConstant.ArticleAction.BrowseArticleAction:
                score = ActionConstant.ArticleAction.BrowseStocksActionScore;
                break;
            case ActionConstant.ArticleAction.PushArticleOpenAction:
                score = ActionConstant.ArticleAction.PushArticleOpenActionScore;
                break;
            case ActionConstant.ArticleAction.CollectionArticleAction:
                score = ActionConstant.ArticleAction.CollectionArticleActionScore;
                break;
            case ActionConstant.ArticleAction.CommentArticleAction:
                score = ActionConstant.ArticleAction.CommentArticleActionScore;
                break;
            case ActionConstant.ArticleAction.SearchArticleAction:
                score = ActionConstant.ArticleAction.SearchArticleActionScore;
                break;
            case ActionConstant.ArticleAction.ShareArticleAction:
                score = ActionConstant.ArticleAction.ShareArticleActionScore;
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

    public static ArticleMapFuntion create() {
        return new ArticleMapFuntion();
    }
}


