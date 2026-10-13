package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Estimates from the supplied World dataset; not current population statistics. */
public final class LanguageReport {
    private LanguageReport() {}

    public record Row(String language, BigDecimal speakers, BigDecimal worldPercentage) {}

    /** Reuses the caller's connection and closes only resources owned by this report. */
    public static List<Row> fetch(Connection connection) throws SQLException, IOException {
        String sql;
        try (var source = LanguageReport.class.getResourceAsStream("/reports/languages/report.sql")) {
            if (source == null) throw new IOException("Language report SQL resource is missing");
            sql = new String(source.readAllBytes(), StandardCharsets.UTF_8);
        }
        List<Row> rows = new ArrayList<>();
        try (var statement = connection.prepareStatement(sql);
             var result = statement.executeQuery()) {
            while (result.next()) {
                rows.add(new Row(result.getString("language"),
                        result.getBigDecimal("estimated_speakers"),
                        result.getBigDecimal("world_population_percentage")));
            }
        }
        return rows;
    }

    public static void print(List<Row> rows, PrintStream out) {
        out.println("Language speakers — estimates from the World sample dataset");
        out.printf("%-12s %20s %22s%n", "Language", "Estimated speakers", "World population (%)");
        for (Row row : rows) {
            String percentage = row.worldPercentage() == null ? "N/A"
                    : row.worldPercentage().setScale(2, RoundingMode.HALF_UP).toPlainString();
            out.printf(Locale.ROOT, "%-12s %,20d %22s%n", row.language(),
                    row.speakers().setScale(0, RoundingMode.HALF_UP).toBigIntegerExact(), percentage);
        }
    }
}
