package com.wallstcn.redis;

import com.wallstcn.util.Property;
import com.wallstcn.util.connection.ConnectionPool;
import com.wallstcn.util.connection.ConnectionPoolBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool extends ConnectionPoolBase<Jedis> implements ConnectionPool<Jedis> {

    private static final Logger logger = LoggerFactory.getLogger(RedisPool.class);

    private static RedisPool pool;

    private static final long serialVersionUID = -9126420905798370249L;

    private static ThreadLocal<Jedis> local=new ThreadLocal<>();

    public RedisPool(final JedisPoolConfig poolConfig, final String host, int port, int timeOut) {
        super(poolConfig, new RedisConnectionFactory(host,port,timeOut));
    }

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置池配置项值
        config.setMaxTotal(Property.getIntValue("redis.pool.maxActive"));
        config.setMaxIdle(Property.getIntValue("redis.pool.maxIdle"));
        config.setMaxWaitMillis(Property.getIntValue("redis.pool.maxWait"));
        config.setTestOnBorrow(Property.getBooleanValue("redis.pool.testOnBorrow"));
        config.setTestOnReturn(Property.getBooleanValue("redis.pool.testOnReturn"));
        pool = new RedisPool(config,Property.getValue("redis.host"),Property.getIntValue("redis.port"),Property.getIntValue("redis.timeout"));
        logger.debug("RedisPool initial success");
    }

    public static Jedis get() {
        return pool.getConnection();
    }

    @Override
    public Jedis getConnection() {
        Jedis jedis =local.get();
        if(jedis==null){
            jedis = super.getResource();
            local.set(jedis);
        }
        return jedis;
    }

    @Override
    public void returnConnection(Jedis conn) {
        super.returnResource(conn);
    }

    @Override
    public void invalidateConnection(Jedis conn) {
        super.invalidateResource(conn);
    }
}
