package com.wallstcn.hbase;

import com.wallstcn.util.connection.ConnectionException;
import com.wallstcn.util.connection.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * @author xiangdao
 */
public class HBaseConnectionFactory implements ConnectionFactory<Connection>{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4024923894283696465L;

    /**
     * hadoopConfiguration
     */
    private final Configuration hadoopConfiguration;

    /**
     * <p>Title: HbaseConnectionFactory</p>
     * <p>Description: 构造方法</p>
     *
     * @param hadoopConfiguration hbase配置
     */
    public HBaseConnectionFactory(final Configuration hadoopConfiguration) {
        this.hadoopConfiguration = hadoopConfiguration;
    }

    /**
     * <p>Title: HbaseConnectionFactory</p>
     * <p>Description: 构造方法</p>
     *
     * @param host    zookeeper地址
     * @param port    zookeeper端口
     * @param master  hbase主机
     * @param rootdir hdfs数据目录
     */
    public HBaseConnectionFactory(final String host, final String port, final String master, final String rootdir) {

        this.hadoopConfiguration = new Configuration();

        if (host == null)
            throw new ConnectionException("[" + HBaseConfig.ZOOKEEPER_QUORUM_PROPERTY + "] is required !");
        this.hadoopConfiguration.set(HBaseConfig.ZOOKEEPER_QUORUM_PROPERTY, host);

        if (port == null)
            throw new ConnectionException("[" + HBaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY + "] is required !");
        this.hadoopConfiguration.set(HBaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY, port);

        if (master != null)
            this.hadoopConfiguration.set(HBaseConfig.MASTER_PROPERTY, master);

        if (rootdir != null)
            this.hadoopConfiguration.set(HBaseConfig.ROOTDIR_PROPERTY, rootdir);
    }

    /**
     * @param properties 参数配置
     * @since 1.2.1
     */
    public HBaseConnectionFactory(final Properties properties) {

        this.hadoopConfiguration = new Configuration();

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {

            this.hadoopConfiguration.set((String) entry.getKey(), (String) entry.getValue());
        }
    }

    @Override
    public PooledObject<Connection> makeObject() throws Exception {

        Connection connection = this.createConnection();

        return new DefaultPooledObject<Connection>(connection);
    }

    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {

        Connection connection = p.getObject();

        if (connection != null)

            connection.close();
    }

    @Override
    public boolean validateObject(PooledObject<Connection> p) {

        Connection connection = p.getObject();

        if (connection != null)

            return ((!connection.isAborted()) && (!connection.isClosed()));

        return false;
    }

    @Override
    public void activateObject(PooledObject<Connection> p) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void passivateObject(PooledObject<Connection> p) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public Connection createConnection() throws Exception {

        Connection connection = org.apache.hadoop.hbase.client.ConnectionFactory
                .createConnection(hadoopConfiguration);

        return connection;
    }
}

