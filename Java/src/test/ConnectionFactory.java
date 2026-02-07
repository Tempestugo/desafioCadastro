package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConncection() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/anime_store";
        String username = "root";
        String password = "XQDprXg$@gHH!AUz";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do MySQL não encontrado. Verifique se a dependência está no pom.xml e se o projeto foi recarregado.", e);
        }
        
        return DriverManager.getConnection(url, username, password);
    }
}
