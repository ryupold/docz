/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael
 */
public abstract class DB {

    public static final String dbPath = "db.sqlite";

    private DB() {
    }

    public static List<Integer> insert(String sql, boolean returnAutoIncrementKeys) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        Statement st = c.createStatement();
        List<Integer> generatedKeys = new ArrayList<>();
        if (returnAutoIncrementKeys && st.execute(sql, returnAutoIncrementKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS)) {
            ResultSet rs = st.getGeneratedKeys();
            while (rs.next()) {
                generatedKeys.add(rs.getInt(0));
            }
            rs.close();
        }

        st.close();
        c.close();

        return returnAutoIncrementKeys ? generatedKeys : null;
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
            statement.close();
            connection.close();
        }

    }
}
