package org.example.pokedex;

import java.sql.*;

public class DbController {
    public static Connection databaseLink;

    private DbController() {
        if (databaseLink == null) databaseLink = getConnection();
    }

    public static Connection getInstance() {
        if (databaseLink == null) new DbController();
        return databaseLink;
    }

    public Connection getConnection() {
        String databaseName = "hasibdb1";
        String databaseUser = "root";
        String databasePassword = "Hasib@102";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return databaseLink;
    }
}
