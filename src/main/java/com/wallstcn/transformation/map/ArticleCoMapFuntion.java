package com.wallstcn.transformation.map;

import com.wallstcn.common.ActionConstant;
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
        if (score == null || score == 0) {
            logger.error("score ArticleCoMapFuntion >>>>>>>>>>>> data  empty");
            switch (logEntity.getAction()) {
                case ActionConstant.ArticleAction.BrowseArticleAction:
                    score = ActionConstant.ArticleAction.BrowseArticleActionScore;
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


