package database;

import java.sql.SQLException;

/*
 * oppa google style
 */
public interface UserDAO {
    public boolean save(UserDataSet dataSet);
    public UserDataSet find(String attr, String value) throws SQLException;  // find by unique value
}
