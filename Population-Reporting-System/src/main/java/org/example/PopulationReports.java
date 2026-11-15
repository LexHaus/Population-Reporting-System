package org.example;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

/** Parameterised queries for country, city, capital and population requirements. */
public final class PopulationReports {
    private PopulationReports() { }
    public record Table(List<String> headings, List<List<String>> rows) {
        public Table {
            headings = List.copyOf(headings);
            rows = rows.stream().map(List::copyOf).toList();
        }
    }
    record Query(String sql, List<Object> values) { }

    static Query query(ReportRequest request) {
        int n = request.number();
        var values = new ArrayList<Object>();
        String sql;
        if (n <= 22) {
            String select;
            String from;
            String order;
            if (n <= 6) {
                select = "c.Code AS Code, c.Name AS Name, c.Continent AS Continent, c.Region AS Region, c.Population AS Population, capital.Name AS Capital";
                from = "country c LEFT JOIN city capital ON capital.ID=c.Capital";
                order = "c.Population DESC, c.Name ASC, c.Code ASC";
            } else {
                select = "city.Name AS Name, c.Name AS Country, " + (n <= 16 ? "city.District AS District, " : "") + "city.Population AS Population";
                from = n <= 16 ? "city JOIN country c ON c.Code=city.CountryCode" : "country c JOIN city ON city.ID=c.Capital AND city.CountryCode=c.Code";
                order = "city.Population DESC, city.Name ASC, city.ID ASC";
            }
            sql = "SELECT " + select + " FROM " + from + filter(request, values) + " ORDER BY " + order;
            if (request.top()) {
                sql += " LIMIT ?";
                values.add(ReportRequest.positive(request.options().get("top"), "top"));
            }
        } else if (n <= 25) {
            // Aggregate city population per country before joining, so country
            // totals are not multiplied by the number of cities in each country.
            String group = switch (n) { case 23 -> "c.Continent"; case 24 -> "c.Region"; default -> "c.Code, c.Name"; };
            String name = n == 23 ? "c.Continent" : n == 24 ? "c.Region" : "c.Name";
            sql = "SELECT " + name + " AS Name, SUM(c.Population) AS Total_population, "
                    + "SUM(COALESCE(urban.population,0)) AS In_cities, "
                    + "100.0*SUM(COALESCE(urban.population,0))/NULLIF(SUM(c.Population),0) AS In_cities_percent, "
                    + "SUM(c.Population)-SUM(COALESCE(urban.population,0)) AS Outside_cities, "
                    + "100.0*(SUM(c.Population)-SUM(COALESCE(urban.population,0)))/NULLIF(SUM(c.Population),0) AS Outside_cities_percent "
                    + "FROM country c LEFT JOIN (SELECT CountryCode, SUM(Population) AS population FROM city GROUP BY CountryCode) urban ON urban.CountryCode=c.Code "
                    + "GROUP BY " + group + " ORDER BY Total_population DESC, Name ASC";
        } else if (n <= 31) {
            if (n == 31) {
                sql = "SELECT city.ID AS ID, city.Name AS Name, c.Name AS Country, city.District AS District, city.Population AS Population FROM city JOIN country c ON c.Code=city.CountryCode WHERE city.ID=?";
                values.add(ReportRequest.positive(request.options().get("city-id"), "city-id"));
            } else if (n == 30) {
                sql = "SELECT city.District AS Name, c.Name AS Country, SUM(city.Population) AS Population FROM city JOIN country c ON c.Code=city.CountryCode"
                        + filter(request, values) + " GROUP BY c.Code, c.Name, city.District";
            } else if (n == 26) {
                sql = "SELECT 'World' AS Name, COALESCE(SUM(c.Population),0) AS Population FROM country c";
            } else {
                String group = n == 27 ? "c.Continent" : n == 28 ? "c.Region" : "c.Code, c.Name";
                String name = n == 27 ? "c.Continent" : n == 28 ? "c.Region" : "c.Name";
                sql = "SELECT " + name + " AS Name, SUM(c.Population) AS Population FROM country c"
                        + filter(request, values) + " GROUP BY " + group + " ORDER BY Name";
            }
        } else throw new IllegalArgumentException("US32 uses LanguageReport");
        return new Query(sql, List.copyOf(values));
    }

    private static String filter(ReportRequest request, List<Object> values) {
        var options = request.options();
        return switch (request.scope()) {
            case "world" -> "";
            case "continent" -> { values.add(options.get("continent")); yield " WHERE c.Continent=?"; }
            case "region" -> { values.add(options.get("region")); yield " WHERE c.Region=?"; }
            case "country" -> {
                values.add(options.get("country")); values.add(options.get("country"));
                yield " WHERE (c.Code=? OR c.Name=?)";
            }
            case "district" -> {
                values.add(options.get("district")); values.add(options.get("country")); values.add(options.get("country"));
                yield " WHERE city.District=? AND (c.Code=? OR c.Name=?)";
            }
            default -> throw new IllegalArgumentException("Unsupported report scope");
        };
    }

    public static Table fetch(Connection connection, ReportRequest request) throws SQLException {
        Query query = query(request);
        try (var statement = connection.prepareStatement(query.sql())) {
            for (int i = 0; i < query.values().size(); i++) statement.setObject(i + 1, query.values().get(i));
            try (var result = statement.executeQuery()) {
                var meta = result.getMetaData();
                var headings = new ArrayList<String>();
                for (int i = 1; i <= meta.getColumnCount(); i++) headings.add(meta.getColumnLabel(i));
                var rows = new ArrayList<List<String>>();
                while (result.next()) {
                    var row = new ArrayList<String>();
                    for (int i = 1; i <= headings.size(); i++) {
                        String value = result.getString(i);
                        if (value == null) value = "N/A";
                        else if (headings.get(i - 1).endsWith("_percent"))
                            value = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toPlainString();
                        row.add(value);
                    }
                    rows.add(row);
                }
                return new Table(headings, rows);
            }
        }
    }

    public static void print(Table table, ReportRequest request, PrintStream out) {
        out.println(ReportRequest.id(request.number()) + " — World sample dataset");
        out.println(String.join("\t", table.headings()));
        for (var row : table.rows()) out.println(String.join("\t", row));
        out.println("Rows: " + table.rows().size());
        if (table.rows().isEmpty()) out.println("No matching records.");
    }
}
