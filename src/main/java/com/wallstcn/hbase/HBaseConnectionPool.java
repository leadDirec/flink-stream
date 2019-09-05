package com.wallstcn.hbase;

import com.wallstcn.util.connection.ConnectionPool;
import com.wallstcn.util.connection.ConnectionPoolBase;
import com.wallstcn.util.connection.ConnectionPoolConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import java.util.Properties;

/**
 * @author xiangdao
 */
public class HBaseConnectionPool extends ConnectionPoolBase<Connection>  implements ConnectionPool<Connection> {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -9126420905798370243L;

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 默认构造方法</p>
     */
    public HBaseConnectionPool() {

        this(HBaseConfig.DEFAULT_HOST, HBaseConfig.DEFAULT_PORT);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param host 地址
     * @param port 端口
     */
    public HBaseConnectionPool(final String host, final String port) {

        this(new ConnectionPoolConfig(), host, port, HBaseConfig.DEFAULT_MASTER, HBaseConfig.DEFAULT_ROOTDIR);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param host    地址
     * @param port    端口
     * @param master  hbase主机
     * @param rootdir hdfs目录
     */
    public HBaseConnectionPool(final String host, final String port, final String master, final String rootdir) {

        this(new ConnectionPoolConfig(), host, port, master, rootdir);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param hadoopConfiguration hbase配置
     */
    public HBaseConnectionPool(final Configuration hadoopConfiguration) {

        this(new ConnectionPoolConfig(), hadoopConfiguration);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param host       地址
     * @param port       端口
     */
    public HBaseConnectionPool(final ConnectionPoolConfig poolConfig, final String host, final String port) {

        this(poolConfig, host, port, HBaseConfig.DEFAULT_MASTER, HBaseConfig.DEFAULT_ROOTDIR);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig          池配置
     * @param hadoopConfiguration hbase配置
     */
    public HBaseConnectionPool(final ConnectionPoolConfig poolConfig, final Configuration hadoopConfiguration) {

        super(poolConfig, new HBaseConnectionFactory(hadoopConfiguration));
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param host       地址
     * @param port       端口
     * @param master     hbase主机
     * @param rootdir    hdfs目录
     */
    public HBaseConnectionPool(final ConnectionPoolConfig poolConfig, final String host, final String port, final String master, final String rootdir) {

        super(poolConfig, new HBaseConnectionFactory(host, port, master, rootdir));
    }

    /**
     * @param poolConfig 池配置
     * @param properties 参数配置
     * @since 1.2.1
     */
    public HBaseConnectionPool(final ConnectionPoolConfig poolConfig, final Properties properties) {

        super(poolConfig, new HBaseConnectionFactory(properties));
    }

    @Override
    public Connection getConnection() {

        return super.getResource();
    }

    @Override
    public void returnConnection(Connection conn) {

        super.returnResource(conn);
    }

    @Override
    public void invalidateConnection(Connection conn) {

        super.invalidateResource(conn);
    }


}
