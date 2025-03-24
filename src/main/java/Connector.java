import java.sql.*;

public class Connector {
    private static Connection connection;
    static {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tripadvisor", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public ResultSet readObjectFromRule(String table, String rule) {
        String query = "SELECT * FROM " + table + " WHERE " + rule;
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public void insertObject(String table, String fields, String values) {
        String query = "INSERT INTO " + table + "(" + fields + ") VALUES " + values;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public void deleteObject(String table, int id) {
        String query = "DELETE FROM " + table + " WHERE id=" + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
