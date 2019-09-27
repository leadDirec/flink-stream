package com.wallstcn.redis;

import com.wallstcn.models.JedisConfig;
import com.wallstcn.util.connection.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author xiangdao
 */
public class RedisConnectionFactory   implements ConnectionFactory<Jedis> {

    private static final Logger logger = LoggerFactory.getLogger(RedisConnectionFactory.class);

    private static final long serialVersionUID = 4024923894283696469L;

    private final JedisConfig jedisConfig;


    /**
     * <p>Title: RedisConnectionFactory</p>
     * <p>Description: 构造方法</p>
     *
     * @param jedisConfig redis置
     */
    public RedisConnectionFactory(final JedisConfig jedisConfig) {
        this.jedisConfig = jedisConfig;
    }

    /**
     * <p>Title: RedisConnectionFactory</p>
     * <p>Description: 构造方法</p>
     */
    public RedisConnectionFactory(final String host, final int port, final int timeOut,final String auth) {
        logger.error("RedisConnectionFactory:::"+host+">>>"+port+">>>>"+auth);
        this.jedisConfig = new JedisConfig();
        this.jedisConfig.setHost(host);
        this.jedisConfig.setPort(port);
        this.jedisConfig.setTimeout(timeOut);
        this.jedisConfig.setAuth(auth);
    }

    @Override
    public Jedis createConnection() throws Exception {
        Jedis connection = new Jedis(jedisConfig.getHost(),jedisConfig.getPort(),jedisConfig.getTimeout());
        if (!StringUtils.isEmpty(jedisConfig.getAuth()) && !StringUtils.isBlank(jedisConfig.getAuth())) {
            connection.auth(jedisConfig.getAuth());
        }

        return connection;
    }

    @Override
    public PooledObject<Jedis> makeObject() throws Exception {
        Jedis connection = this.createConnection();
        return new DefaultPooledObject<Jedis>(connection);
    }

    @Override
    public void destroyObject(PooledObject<Jedis> p) throws Exception {
        Jedis connection = p.getObject();
        if (connection != null)
            connection.close();
    }

    @Override
    public boolean validateObject(PooledObject<Jedis> p) {
        //TODO validateObject
//        Jedis connection = p.getObject();
//        if (connection != null)
//            return connection.isConnected();
//        return false;
        return true;
    }

    @Override
    public void activateObject(PooledObject<Jedis> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<Jedis> p) throws Exception {

    }
}
