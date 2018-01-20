package model.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2018 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : model</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2018年01月19日</li>
 * <li>@author     : zzy0_0</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class MySqlDb extends Db {
    private static MySqlDb instance;

    public synchronized static MySqlDb getInstance() {
        try {
            if (instance == null) {
                return new MySqlDb();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    @Override
    public Connection getConnect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/tr18?useUnicode=true;characterEncoding=UTF8", "root", "root");
    }
}