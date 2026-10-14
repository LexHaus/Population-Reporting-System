package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Runs the team's existing US07–US16 queries against the World sample dataset. */
public final class CityReport {
    private CityReport() {}

    public record Row(String name, String country, String district, long population) {}

    public static int reportNumber(String id) {
        if (!id.matches("US(0[7-9]|1[0-6])")) {
            throw new IllegalArgumentException("City report must be US07 through US16");
        }
        return Integer.parseInt(id.substring(2));
    }

    static String query(String id) throws IOException {
        int number = reportNumber(id);
        try (var source = CityReport.class.getResourceAsStream("/reports/cities/report.sql")) {
            if (source == null) throw new IOException("City report SQL resource is missing");
            String sql = new String(source.readAllBytes(), StandardCharsets.UTF_8);
            String marker = "-- " + id + " -";
            int start = sql.indexOf(marker);
            int end = sql.indexOf(';', start);
            if (start < 0 || end < 0) throw new IOException("SQL missing for city report " + number);
            return sql.substring(start, end + 1);
        }
    }

    /** Leaves the caller's connection open. */
    public static List<Row> fetch(Connection connection, String id) throws SQLException, IOException {
        var rows = new ArrayList<Row>();
        try (var statement = connection.prepareStatement(query(id));
             var result = statement.executeQuery()) {
            while (result.next()) {
                rows.add(new Row(result.getString("name"), result.getString("country"),
                        result.getString("district"), result.getLong("population")));
            }
        }
        return rows;
    }

    public static void print(List<Row> rows, String id, PrintStream out) {
        out.println("City population report " + id + " — World sample dataset");
        out.printf("%-35s %-35s %-25s %15s%n", "Name", "Country", "District", "Population");
        for (Row row : rows) {
            out.printf(Locale.ROOT, "%-35s %-35s %-25s %,15d%n",
                    row.name(), row.country(), row.district(), row.population());
        }
        out.println("Rows: " + rows.size());
    }
}
