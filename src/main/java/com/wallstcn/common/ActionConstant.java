package com.wallstcn.common;

public class ActionConstant {

    //标签相关
    public  static class UserLabel {
        public static final  int ShortTermUsersPunchBoard = 1001; //短线用户(打板)
        public static final  int ShortTermUsersSubjectMatter = 1002; //短线用户(题材)
        public static final  int MidlineUsersIndustry = 2001; //中线用户（行业)
        public static final  int LongTermUsersCompanies = 3001; //长线用户（公司)
        public static final  int EnthusiastsTechnophile = 4001; //技术爱好者
        public static final  int SmallWhiteUsers = 5001; //小白用户
    }


    //股票相关
    public static class StockAction {
        public static final String ActionType = "stocks";

        public static final int BrowseStocksAction = 3001;//浏览股票
        public static final Double BrowseStocksActionScore = 1.0;//浏览股票得分

        public static final int BrowseStocksFrequencyAction = 3002;//多次浏览股票
        public static final Long BrowseStocksFrequencyCount = 3L;//多次浏览股票次数
        public static final Double BrowseStocksFrequencyActionScore = 3.0;

        public static final int UserListOptionalStocksAction = 3003;//自选股列表包含符合条件
        public static final Double UserListOptionalStocksActionScore = 3.0;

        public static final int SearchStocksAction = 3004;//搜索符合条件的股票
        public static final Double SearchStocksActionScore = 5.0;
    }

    //文章相关
    public static class ArticleAction {
        public static final String ActionType = "article";

        public static final int BrowseArticleAction = 2001;//浏览文章
        public static final Double BrowseStocksActionScore = 1.0;

        public static final int PushArticleOpenAction = 2002;//推送打开符合条件
        public static final Double PushArticleOpenActionScore = 2.0;

        public static final int CollectionArticleAction = 2003;//收藏符合条件
        public static final Double CollectionArticleActionScore = 3.0;

        public static final int CommentArticleAction = 2004;//评论符合条件
        public static final Double CommentArticleActionScore = 4.0;

        public static final int SearchArticleAction = 2005;//搜索符合条件
        public static final Double SearchArticleActionScore = 5.0;

        public static final int ShareArticleAction = 2006;//分享符合条件
        public static final Double ShareArticleActionScore = 10.0;
    }

    //功能行为相关
    public static class FeaturesAction {
        public static final String ActionType = "features";

        public static final int HavePurchasedShortTermLabelPaymentColumn = 4001;//曾购买过短线标签付费栏目
        public static final Double HavePurchasedShortTermLabelPaymentColumnScore = 5.0;

        public static final int ClickOnTheAlterationInTheDis = 4002;//点击过盘中异动
        public static final Double ClickOnTheAlterationInTheDisScore = 5.0;

        public static final int ClickOnTheListOfDragonsAndTiger = 4003;//点击过龙虎榜
        public static final Double ClickOnTheListOfDragonsAndTigerScore = 5.0;

        public static final int ClickOnTheOverRisingStopPool = 4004;//	   点击过涨停池
        public static final Double ClickOnTheOverRisingStopPoolScore = 5.0;

        public static final int ClickOnTheFryerPool = 4005;//	   点击过炸板池
        public static final Double ClickOnTheFryerPoolScore = 5.0;

        public static final int HavePurchasedSubjectLabelPaymentColumn = 4006;//曾购买过题材标签付费栏目
        public static final Double HavePurchasedSubjectLabelPaymentColumnScore = 5.0;

        public static final int BrowseSectionPage = 4008;//浏览板块页
        public static final Double BrowseSectionPageScore = 5.0;

        public static final int SearchSectionPage = 4009;//浏览板块页
        public static final Double SearchSectionPageScore = 5.0;

        public static final int HavePurchasedIndustryLabelPaymentColumn = 4010;//曾购买过行业标签付费栏目
        public static final Double HavePurchasedIndustryLabelPaymentColumnScore = 5.0;

        public static final int ClickOnTheResearchPaper = 4011;//	点击研报
        public static final Double ClickOnTheResearchPaperScore = 5.0;

        public static final int HavePurchasedNewcomerLabelPaymentColumn = 4012;//曾购买过新手标签付费栏目
        public static final Double HavePurchasedNewcomerLabelPaymentColumnScore = 5.0;
    }

}
