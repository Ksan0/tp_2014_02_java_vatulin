package database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

/*
 * oppa google style
 */
public class UserDataSetDAO implements UserDAO {
    private final Connection conn;

    public UserDataSetDAO(Connection conn) {
        this.conn = conn;
    }

    public UserDataSet find(String attr, String value) throws SQLException {
        String queryTemplate = "SELECT * FROM users WHERE %s = '%s'";
        String query = String.format(queryTemplate, attr, value);

        UserDataSet user = null;
        int dataRows = 0;

        try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                dataRows++;
                user = new UserDataSet(rs.getLong("userID"), rs.getString("login"), rs.getString("password"));
            }
        } catch (SQLException e) {
            return null;
        }

        if (dataRows >= 2) {
            String msg = String.format("'%s' attr isn't unique", attr);
            throw new SQLException(msg);
        }

        return user;
    }

    public boolean save(UserDataSet user) {
        String queryTemplate;
        String query;
        if (user.isIdDefined()) {
            queryTemplate = "UPDATE users SET login = '%s', password = '%s' WHERE userID = %s";
            query = String.format(queryTemplate, user.getLogin(), user.getPassword(), user.getId());
        } else {
            queryTemplate  = "INSERT INTO users (login, password) VALUES ('%s', '%s')";
            query = String.format(queryTemplate, user.getLogin(), user.getPassword());
        }

        try(Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        } catch(SQLException e) {
            return false;
        }
        return true;
    }

}
