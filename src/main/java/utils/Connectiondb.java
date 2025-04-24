/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectiondb {
    private static final String URL = "jdbc:postgresql://localhost:5432/db_voiture";
    private static final String user = "postgres";
    private static final String password = "postgres";

    public static Connection connecter() throws SQLException {
        try {
            return DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            throw new SQLException("❌ Erreur de connexion à la base de données", e);
        }
    }

    // ➕ On fait pointer getconnection() vers connecter()
    public static Connection getconnection() throws SQLException {
        return connecter();
    }
}
