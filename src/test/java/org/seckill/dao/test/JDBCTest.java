package org.seckill.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Description: 使用JDBC进行测试数据库连接
 *
 * @author: dong
 * @create: 2017-03-09-11:12
 */
@RunWith(JUnit4.class)
public class JDBCTest {
    private String driver = null;
    private String url = null;
    private String username = null;
    private String password = null;

    @Test
    public void testJdbcConnect() throws IOException, SQLException, ClassNotFoundException {
        InputStream in = JDBCTest.class.getResourceAsStream("/jdbc.properties");
        Properties prop = new Properties();
        prop.load(in);           // 通过load方法加载jdbc.properties配置文件

        System.out.println(prop.get("driver"));
        this.driver = (String)prop.get("driver");
        this.url = (String)prop.get("url");
        this.username = (String)prop.get("username");
        this.password = (String)prop.get("password");

        // 加载JDBC数据库驱动
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, username, password);
        System.out.println(conn);       // 向控制台打印出连接数据库的驱动对象.
    }
}
