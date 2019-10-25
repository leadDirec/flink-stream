package com.wallstcn.transformation.source;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


/**
 * @author xiangdao
 */
public class MysqlSource extends RichSourceFunction<Map<Integer,Double>>{
    private static final Logger logger = LoggerFactory.getLogger(MysqlSource.class);
    private String host;
    private Integer port;
    private String db;
    private String user;
    private String passwd;
    private Integer secondInterval;

    private volatile boolean isRunning = true;

    private Connection connection;
    private PreparedStatement preparedStatement;

    public MysqlSource(String host, Integer port, String db, String user, String passwd, Integer secondInterval) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.passwd = passwd;
        this.secondInterval = secondInterval;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        Class.forName("com.mysql.jdbc.Driver");
        connection= DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db+"?useUnicode=true&characterEncoding=UTF-8", user, passwd);
        String sql="select action,score from user_action_conf";
        preparedStatement=connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (connection != null) {
            connection.close();
        }
        if (preparedStatement != null) {
            preparedStatement.close();
        }
    }

    @Override
    public void run(SourceContext<Map<Integer, Double>> sourceContext) throws Exception {
        try {
            while (isRunning) {
                HashMap output = new  HashMap<Integer,Double>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Integer action = resultSet.getInt("action");
                    Double score = resultSet.getDouble("score");
                    output.put(action,score);
                }
                sourceContext.collect(output);
                //每隔多少秒执行一次查询
                Thread.sleep(1000*secondInterval);
            }
        }catch (Exception e) {
            logger.error("从Mysql获取配置异常...",e);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
