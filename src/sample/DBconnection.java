package sample;


import java.sql.*;

public class DBconnection {
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DB_NAME = "authorizationfxproject";
    private final String LOGIN = "root";
    private final String PASS = "514146454";

    private Connection dbConn = null;

    private Connection getConn() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME +
                "?characterEncoding=UTF-8&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    public void isConnected() throws SQLException, ClassNotFoundException {
        dbConn = getConn();
        System.out.println(dbConn.isValid(1000));
    }

    public boolean regUser(String login, String email, String password) {
        String sql = "INSERT INTO `users` (`login`, `email`, `password`) VALUES (?, ?, ?)";
        try {
            Statement statement = getConn().createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM `users` WHERE LOGIN = '" + login + "' LIMIT 1");
            if (resultSet.next())
                return false;

            PreparedStatement preparedStatement = getConn().prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return true;

    }

    public boolean authUser(String login, String password) throws SQLException, ClassNotFoundException {
        Statement statement = getConn().createStatement();
        String sql = "SELECT * FROM `users` WHERE `login` = '" + login + "' AND `password` = '" + password + "' LIMIT 1";
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.next();
    }

    public ResultSet getArticles() {
        String sql = "SELECT `title`, `intro` FROM `articles`";
        Statement statement = null;
        ResultSet res = null;
        try {
            statement = getConn().createStatement();
            res = statement.executeQuery(sql);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return res;

    }

    public void addArtcile(String title, String intro, String text) throws SQLException, ClassNotFoundException {
        String sql="INSERT INTO `articles` (`title`, `intro`, `text`) VALUES (?, ? ,?)";
        PreparedStatement preparedStatement=getConn().prepareStatement(sql);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, intro);
        preparedStatement.setString(3,text);
        preparedStatement.executeUpdate();
    }
}