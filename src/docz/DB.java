/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.tools.ChangeFileEncryption;

/**
 *
 * @author Michael
 */
public abstract class DB {

    private static final String dbPath = "db/db";
    private static String pw = null;

    protected static void setPW(String pw) {
        DB.pw = pw;
    }

    public static final String getDBPath() {
        return new File(dbPath).getAbsolutePath();
    }

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            Log.l(ex);
            Log.l("ERROR: no sqlite driver found");
        }
    }

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

    public static List<Long> insert(String sql, boolean returnAutoIncrementKeys) throws SQLException {
        Connection c = createConnection();
        c.setAutoCommit(false);
        Statement st = c.createStatement();
        st.execute(sql, returnAutoIncrementKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

        List<Long> generatedKeys = null;

        if (returnAutoIncrementKeys) {
            generatedKeys = new LinkedList<>();
            try (ResultSet grs = st.getGeneratedKeys()) {
                while (grs.next()) {
                    generatedKeys.add(grs.getLong(1));
                }
            }
        }

        c.commit();
        st.close();
        c.close();

        return generatedKeys;
    }

    public static int update(String sql) throws SQLException {

        Connection c = createConnection();
        Statement st = c.createStatement();

        int updatedRows = st.executeUpdate(sql);

        st.close();
        c.close();

        return updatedRows;
    }

    public static DBResult select(String sql) throws SQLException {

        Connection c = createConnection();
        Statement st = c.createStatement();

        ResultSet rs = st.executeQuery(sql);

        return new DBResult(c, st, rs);
    }

    public static Connection createConnection() throws SQLException {
        if (pw != null) {
            Connection c = DriverManager.getConnection("jdbc:h2:file:" + new File(dbPath).getAbsolutePath() + ";CIPHER=AES", "sa", pw + " " + "password");
            return c;
        }

        throw new SecurityException("no password entered!");
    }

    public static boolean changePW(String oldPW, String newPW) throws SQLException {
        if (oldPW != null && newPW != null) {
            try {
                ChangeFileEncryption.execute(new File(DB.getDBPath()).getParent(), null, "AES", oldPW.toCharArray(), newPW.toCharArray(), false);
                DB.setPW(newPW);
                return true;
            } catch (SQLException sqlex) {
                if (sqlex.getMessage().contains("Encryption error")) {
                    return false;
                } else {
                    throw sqlex;
                }
            }
        }

        throw new SecurityException("no password entered!");
    }

    public static class DBResult implements AutoCloseable {

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
