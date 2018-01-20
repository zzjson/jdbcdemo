package mybatisdemo;

import entity.Dept;
import model.jdbc.MySqlDb;
import model.jdbc.ParamHandler;
import model.jdbc.ResultHandler;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
public class SqlExecute {
    static MySqlDb mySqlDb = MySqlDb.getInstance();

    public static void main(String[] args) {
        Dept d = new Dept(1l, "4", "3");
        Class clazz = Dept.class;
        Field[] fields = clazz.getDeclaredFields();

        int j = 0;
        try {
            j = update(d, "id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void select() {
        try {
            List<Dept> deptList = mySqlDb.executeQuery("select * from dept", new ResultHandler<Dept>() {
                public Dept dohandler(Map<String, Object> row) {
                    Long id = (Long) row.get("id");
                    String dept_name = (String) row.get("dept_name");
                    String role = (String) row.get("role");
                    return new Dept(id, dept_name, role);
                }
            });
            for (Dept dept : deptList) {
                System.out.println(dept);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int insert(final Object o) throws SQLException {
        Class clazz = o.getClass();
        String tableName = clazz.getSimpleName();
        final Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for (Field field : fields) {
            sb1.append(field.getName());
            sb1.append(",");
            sb2.append("?,");
        }
        sb1.delete(sb1.length() - 1, sb1.length());
        sb2.delete(sb2.length() - 1, sb2.length());
        String sql = String.format("insert into %s (%s) values(%s)", tableName, sb1, sb2);
        return mySqlDb.executeUpdate(sql, new ParamHandler() {
            public void doHandler(PreparedStatement preparedStatement) throws SQLException {
                for (int i = 0; i < fields.length; i++) {
                    Field filed = fields[i];
                    filed.setAccessible(true);
                    try {
                        preparedStatement.setObject(i + 1, filed.get(o));
                    } catch (IllegalAccessException e) {
                        System.out.println("field错误" + filed.getName());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static int update(final Object o, final String pk) throws SQLException {
        Class clazz = o.getClass();
        String tableName = clazz.getSimpleName();
        final Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb1 = new StringBuffer();
        String fname = null;
        for (Field field : fields) {
            fname = field.getName();
            if (!fname.equalsIgnoreCase(pk)) {
                sb1.append(fname + " = ? , ");
            }
        }
        sb1.deleteCharAt(sb1.lastIndexOf(","));
        String sql = String.format("update %s set %s  where %s = ?", tableName, sb1, pk);
        System.out.println(sql);
        return mySqlDb.executeUpdate(sql, new ParamHandler() {
            public void doHandler(PreparedStatement preparedStatement) throws SQLException {
                int index = 1;
                Object pkResult = null;
                for (Field field : fields) {
                    String fname = field.getName();
                    field.setAccessible(true);
                    try {
                        if (!fname.equalsIgnoreCase(pk)) {
                            preparedStatement.setObject(index++, field.get(o));
                            System.out.println(field.getName());
                        } else {
                            pkResult = field.get(o);
                        }

                        preparedStatement.setObject(index, pkResult);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}