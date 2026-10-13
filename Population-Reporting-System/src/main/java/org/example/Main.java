package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        if (args.length > 1 || (args.length == 1 && !args[0].equals("languages") && !args[0].equals("countries"))) {
            System.err.println("Usage: java -jar app.jar [countries|languages]");
            System.exit(2);
        }
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
                     DriverManager.getConnection(url, username, password)) {
            if (args.length == 1 && args[0].equals("languages")) {
                LanguageReport.print(LanguageReport.fetch(connection), System.out);
                return;
            }
            try (Statement statement = connection.createStatement();
                 ResultSet results = statement.executeQuery(sql)) {
                System.out.println("Top 10 most populous countries:");
                System.out.println("--------------------------------");
                while (results.next()) {
                    String name = results.getString("Name");
                    long population = results.getLong("Population");
                    System.out.printf("%-35s %,d%n", name, population);
                }
            }
        } catch (Exception e) {
            System.err.println("Could not generate the report from the World database:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
