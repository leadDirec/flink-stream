package com.wallstcn.hbase;

import com.wallstcn.util.connection.ConnectionException;
import com.wallstcn.util.connection.ConnectionPool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xiangdao
 * TODO not implement
 */
public class HBaseSharedConnPool implements ConnectionPool<Connection> { {

}
    private static final long serialVersionUID = 1L;

    private static final AtomicReference<HBaseSharedConnPool> pool = new AtomicReference<HBaseSharedConnPool>();

    private final Connection connection;

    private HBaseSharedConnPool(Configuration configuration) throws IOException {
        this.connection = ConnectionFactory.createConnection(configuration);
    }

    /**
     * Gets instance.
     *
     * @param host    the host
     * @param port    the port
     * @param master  the master
     * @param rootdir the rootdir
     * @return the instance
     */
    public synchronized static HBaseSharedConnPool getInstance(final String host, final String port, final String master, final String rootdir) {

        Properties properties = new Properties();

        if (host == null)
            throw new ConnectionException("[" + HBaseConfig.ZOOKEEPER_QUORUM_PROPERTY + "] is required !");
        properties.setProperty(HBaseConfig.ZOOKEEPER_QUORUM_PROPERTY, host);

        if (port == null)
            throw new ConnectionException("[" + HBaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY + "] is required !");
        properties.setProperty(HBaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY, port);

        if (master != null)
            properties.setProperty(HBaseConfig.MASTER_PROPERTY, master);

        if (rootdir != null)
            properties.setProperty(HBaseConfig.ROOTDIR_PROPERTY, rootdir);

        return getInstance(properties);
    }

    /**
     * Gets instance.
     *
     * @param properties the properties
     * @return the instance
     */
    public synchronized static HBaseSharedConnPool getInstance(final Properties properties) {

        Configuration configuration = new Configuration();

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {

            configuration.set((String) entry.getKey(), (String) entry.getValue());
        }

        return getInstance(configuration);
    }

    /**
     * Gets instance.
     *
     * @param configuration the configuration
     * @return the instance
     */
    public synchronized static HBaseSharedConnPool getInstance(final Configuration configuration) {

        if (pool.get() == null)

            try {
                pool.set(new HBaseSharedConnPool(configuration));

            } catch (IOException e) {

                e.printStackTrace();
            }

        return pool.get();
    }

    @Override
    public Connection getConnection() {

        return connection;
    }

    @Override
    public void returnConnection(Connection conn) {

    }

    @Override
    public void invalidateConnection(Connection conn) {

        try {
            if (conn != null)

                conn.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * Close.
     */
    public void close() {

        try {
            connection.close();

            pool.set(null);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}


