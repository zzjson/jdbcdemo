package model.jdbc;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2018 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : model</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2018年01月18日</li>
 * <li>@author     : zzy0_0</li>
 * </ul>
 * <p>****************************************************************************</p>
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作
 */
public abstract class Db implements Jdbc {

    public <T> List<T> executeQuery(String sql, ResultHandler<T> resultHandler) throws SQLException {
        return executeQuery(sql, null, resultHandler);
    }

    public <T> List<T> executeQuery(String sql, ParamHandler paramHandler) throws SQLException {
        return executeQuery(sql, paramHandler, null);
    }

    public <T> List<T> executeQuery(String sql, ParamHandler paramHandler, ResultHandler<T> resultHandler) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;
        List<T> rows = null;
        try {
            connection = getConnect();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute(sql);
            rows = new ArrayList<T>();
            if (paramHandler != null) {
                paramHandler.doHandler(preparedStatement);
            }
            resultset = preparedStatement.executeQuery();
            ResultSetMetaData metaData = preparedStatement.getMetaData();
            while (resultset.next()) {
                Map<String, Object> row = new HashMap();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String label = metaData.getColumnLabel(i);
                    row.put(label, resultset.getObject(label));
                }
                if (resultHandler != null) {
                    rows.add(resultHandler.dohandler(row));
                }
            }
        } finally {
            this.release(connection, preparedStatement, resultset);
        }
        return rows;
    }

    public int executeUpdate(String sql, ParamHandler paramHandler) throws SQLException {
        int result = 0;
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = getConnect();
            preparedStatement = con.prepareStatement(sql);
            if (paramHandler != null) {
                paramHandler.doHandler(preparedStatement);
            }
            result = preparedStatement.executeUpdate();
        } finally {
            release(con, preparedStatement, null);
        }
        return result;
    }

    void release(Connection con, PreparedStatement ps, ResultSet resultset) {
        if (resultset != null) {
            try {
                resultset.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    abstract Connection getConnect() throws SQLException;


}