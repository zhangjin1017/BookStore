package utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author 1216916137
 */
public class DBUtilsByDruid {
    private static DruidDataSource druidDataSource;
//初始化德鲁伊连接池
    static {
//getClassLoader();得到类加载器对象(单例)
        InputStream is = DBUtilsByDruid.class.getClassLoader().getResourceAsStream("druid.properties");//得到信息流
        Properties properties = new Properties();//配置文件对象
        try {
            properties.load(is);//读取配置文件
            if (is != null) {
                is.close();
            }
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);//加载配置文件,获取德鲁伊连接池
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取数据库连接对象
    public static Connection getConnection() throws SQLException {
        return  druidDataSource.getConnection();
    }
    //关闭结果集和数据库处理对象(或预处理对象)和数据库连接对象(数据连接池本质上不会被关闭)
    public static void close(ResultSet rs, Statement statement, Connection conn) {
        try {
            if (null != rs) {
                rs.close();
            }
            if (null != statement) {
                statement.close();
            }
            if (null != conn) {
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}