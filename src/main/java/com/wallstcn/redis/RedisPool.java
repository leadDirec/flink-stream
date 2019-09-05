package com.wallstcn.redis;

import com.wallstcn.util.connection.ConnectionPool;
import com.wallstcn.util.connection.ConnectionPoolBase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool extends ConnectionPoolBase<Jedis> implements ConnectionPool<Jedis> {

    private static final long serialVersionUID = -9126420905798370249L;

    //把redis连接对象放到本地线程中
    private static ThreadLocal<Jedis> local=new ThreadLocal<>();

    public RedisPool(final JedisPoolConfig poolConfig, final String host, int port, int timeOut) {
        super(poolConfig, new RedisConnectionFactory(host,port,timeOut));
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
