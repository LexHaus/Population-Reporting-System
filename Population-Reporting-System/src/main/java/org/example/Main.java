package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        String host = System.getenv().getOrDefault("DB_HOST", "localhost");
        String port = System.getenv().getOrDefault("DB_PORT", "3306");
        String database = System.getenv().getOrDefault("DB_NAME", "world");
        String username = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        String sql = """
                SELECT Name, Population
                FROM country
                ORDER BY Population DESC
                LIMIT 10
                """;

        try (Connection connection =
                     DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            System.out.println("Top 10 most populous countries:");
            System.out.println("--------------------------------");

            while (results.next()) {
                String name = results.getString("Name");
                long population = results.getLong("Population");

                System.out.printf("%-35s %,d%n", name, population);
            }

        } catch (Exception e) {
            System.err.println("Could not connect to the World database:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
