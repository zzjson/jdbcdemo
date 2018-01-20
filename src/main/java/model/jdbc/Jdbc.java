package model.jdbc;

import java.sql.SQLException;
import java.util.List;

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
public interface Jdbc {
    <T> List<T> executeQuery(String sql, ParamHandler paramHandler, ResultHandler<T> resultHandler) throws SQLException;

    int executeUpdate(String sql, ParamHandler paramHandler) throws SQLException;
}