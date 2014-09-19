/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public abstract class DB {

    public static final String dbPath = "db.sqlite";

    private DB() {
    }

    public static String getSetting(String name, String defaultValue) {
        String v = getSetting(name);
        return v == null ? defaultValue : v;
    }

    public static String getSetting(String name) {
        DBResult r = null;
        try {
            r = select("select value from settings where name='" + name + "';");

            if (r != null) {
                if (r.resultSet.next()) {
                    return r.resultSet.getString(1);
                }
            }
        } catch (SQLException ex) {
            Log.l(ex);
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (SQLException ex) {
                    Log.l(ex);
                }
            }
        }
        return null;
    }

    public static void setSetting(String name, String value) {
        try {
            if (DB.update("update settings set value='" + value + "' where name='" + name + "'") == 0) {

                DB.insert("insert into settings(name, value) values('" + name + "','" + value + "')", false);
            }
        } catch (SQLException ex) {
            Log.l(ex);
        }
    }

    public static Long insert(String sql, boolean returnAutoIncrementKeys) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        c.setAutoCommit(false);
        Statement st = c.createStatement();
        long generatedKey = 0;
        st.execute(sql);

        if (returnAutoIncrementKeys) {
            ResultSet rs = st.executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                generatedKey = rs.getLong(1);
            }
        }
        c.commit();
        st.close();
        c.close();

        return returnAutoIncrementKeys ? generatedKey : null;
    }

    public static int update(String sql) throws SQLException {

        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        Statement st = c.createStatement();

        int updatedRows = st.executeUpdate(sql);

        st.close();
        c.close();

        return updatedRows;
    }

    public static DBResult select(String sql) throws SQLException {

        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        Statement st = c.createStatement();

        ResultSet rs = st.executeQuery(sql);

        return new DBResult(c, st, rs);
    }

    public static Connection createConnection() throws SQLException {

        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        return c;
    }

    public static class DBResult {

        public final Connection connection;
        public final Statement statement;
        public final ResultSet resultSet;

        public DBResult(Connection connection, Statement statement, ResultSet resultSet) {
            this.connection = connection;
            this.statement = statement;
            this.resultSet = resultSet;
        }

        public void close() throws SQLException {
            resultSet.close();
            statement.close();
            connection.close();
        }

    }
}
