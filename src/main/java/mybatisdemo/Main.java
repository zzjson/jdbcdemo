package mybatisdemo;

import java.sql.*;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2018 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : mybatisdemo</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2018年01月18日</li>
 * <li>@author     : zzy0_0</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class Main {
    private static Connection con;
    private static PreparedStatement ps;
    private static String sql;
    private static ResultSet resultSet;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tr18?useUnicode=true",
                    "root", "root");
            sql = "SELECT  * FROM tr18.dept";
            ps = con.prepareStatement(sql);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getObject(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }


}