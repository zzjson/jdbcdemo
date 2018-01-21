package mybatisdemo;

import entity.Dept;
import model.jdbc.MySqlDb;
import model.jdbc.ParamHandler;
import reflection.Column;
import reflection.Id;
import reflection.Table;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
public class SqlExecute_Reflection {
    static MySqlDb mySqlDb = MySqlDb.getInstance();

    public static void main(String[] args) {
        Dept d = new Dept(6l, "4", "3");
        try {
            int i = insert(d);
            System.out.println(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static String getAnnoationField(Field field) {
        String fieldName = field.getName();
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            fieldName = column.name();
        }
        return fieldName;
    }

    /**
     * 获取表名
     *
     * @param clazz
     * @return
     */
    private static String getTableName(Class clazz) {
        String tableName = clazz.getSimpleName();
        // 判断clazz是否标识了@Table的注解
        if (clazz.isAnnotationPresent(Table.class)) {
            // 获取Table对象
            Table table = (Table) clazz.getDeclaredAnnotation(Table.class);
            // 获取注解属性值
            tableName = table.tableName();
        }
        return tableName;
    }

    /**
     * idField
     *
     * @param clazz
     * @return
     */
    private static Field getIdField(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field idField = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
                break;
            }
        }
        return idField;
    }

    private static List<Object> select(Class<?> clazz) throws SQLException {
        String tableName = getTableName(clazz);
        String sql = String.format("select * from %s", tableName);
        return mySqlDb.executeQuery(sql, row -> {
            Object instance = null;
            Field field[] = clazz.getDeclaredFields();
            try {
                instance = clazz.newInstance();
                for (Field field1 : field) {
                    field1.setAccessible(true);
                    Object column = row.get(getAnnoationField(field1));
                    field1.set(instance, column);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return instance;
        });

    }

    public static int insert(final Object o) throws SQLException {
        Class clazz = o.getClass();
        String tableName = clazz.getSimpleName();
        final Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for (Field field : fields) {
            sb1.append(getAnnoationField(field));
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
        String tableName = getTableName(clazz);
        final Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb1 = new StringBuffer();
        String fname = null;
        for (Field field : fields) {
            fname = getAnnoationField(field);
            if (!fname.equalsIgnoreCase(pk)) {
                sb1.append(fname + " = ? , ");
            }
        }
        sb1.deleteCharAt(sb1.lastIndexOf(","));
        String sql = String.format("update %s set %s  where %s = ?", tableName, sb1, pk);
        System.out.println(sql);
        return mySqlDb.executeUpdate(sql, preparedStatement -> {
            int index = 1;
            Object pkResult = null;
            for (Field field : fields) {
                String fname1 = getAnnoationField(field);
                field.setAccessible(true);
                try {
                    if (!fname1.equalsIgnoreCase(pk)) {
                        preparedStatement.setObject(index++, field.get(o));
                    } else {
                        pkResult = field.get(o);
                    }
                    preparedStatement.setObject(index, pkResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}