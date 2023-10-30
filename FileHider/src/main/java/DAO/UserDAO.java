package DAO;

import DB.ConnectDatabase;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static boolean isExist(String email) throws SQLException {
        Connection connection = ConnectDatabase.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "select email from users");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String em = rs.getString(1);
            if (em.equals(email)) {
                return true;
            }
        }
        return false;
    }
    public static int saveUser(User user) throws SQLException {
        Connection connection = ConnectDatabase.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "insert into users values(Default, ?, ?)");
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        return ps.executeUpdate();
    }
}
