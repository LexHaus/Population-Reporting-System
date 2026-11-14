package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.sql.DriverManager;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Opt-in read-only check against the supplied World sample database. */
@EnabledIfEnvironmentVariable(named = "WORLD_TEST_URL", matches = ".+")
class LanguageReportIntegrationTest {
    @Test void matchesWorldSampleThroughJdbc() throws Exception {
        try (var connection = DriverManager.getConnection(System.getenv("WORLD_TEST_URL"),
                System.getenv().getOrDefault("DB_USER", "root"),
                System.getenv().getOrDefault("DB_PASSWORD", ""))) {
            var rows = LanguageReport.fetch(connection);
            assertEquals(List.of("Chinese", "Hindi", "Spanish", "English", "Arabic"),
                    rows.stream().map(LanguageReport.Row::language).toList());
            String[] counts = {"1191843539", "405633070", "355029462", "347077867.3", "233839238.7"};
            for (int i = 0; i < counts.length; i++) {
                assertEquals(0, new BigDecimal(counts[i]).compareTo(rows.get(i).speakers()));
                double expected = new BigDecimal(counts[i]).doubleValue() / 6078749450L * 100;
                assertEquals(expected, rows.get(i).worldPercentage().doubleValue(), 0.000001);
            }
            assertFalse(connection.isClosed());
        }
    }

    /** A unique disposable schema keeps synthetic data separate from World. */
    @Test void handlesSyntheticEdgeCasesInMysqlWithoutChangingWorldData() throws Exception {
        try (var connection = DriverManager.getConnection(System.getenv("WORLD_TEST_URL"),
                System.getenv().getOrDefault("DB_USER", "root"),
                System.getenv().getOrDefault("DB_PASSWORD", ""));
             var setup = connection.createStatement()) {
            String originalCatalog = connection.getCatalog();
            String schema = "language_test_" + java.util.UUID.randomUUID().toString().replace("-", "");
            setup.execute("CREATE DATABASE " + schema);
            connection.setCatalog(schema);
            try (var data = connection.createStatement()) {
                data.execute("CREATE TABLE country (Code CHAR(3), Population BIGINT)");
                data.execute("CREATE TABLE countrylanguage (CountryCode CHAR(3), Language VARCHAR(50), Percentage DECIMAL(5,2))");
                data.execute("INSERT INTO country VALUES ('AAA',1001),('BBB',1001),('CCC',1998)");
                data.execute("INSERT INTO countrylanguage VALUES ('AAA','English',20),('BBB','English',20),('AAA','Chinese',40),('AAA','French',100)");
                var rows = LanguageReport.fetch(connection);
                assertEquals(List.of("Chinese", "English", "Arabic", "Hindi", "Spanish"),
                        rows.stream().map(LanguageReport.Row::language).toList());
                // Fractional estimates must be summed before rounding; CCC contributes
                // to the world denominator even though it has no language entries.
                assertEquals(0, new BigDecimal("400.4").compareTo(rows.get(1).speakers()));
                assertEquals(10.01, rows.get(1).worldPercentage().doubleValue(), 0.000001);
                assertEquals(0, BigDecimal.ZERO.compareTo(rows.get(2).speakers()));
                assertEquals(0, BigDecimal.ZERO.compareTo(rows.get(2).worldPercentage()));

                data.execute("UPDATE country SET Population=0");
                assertTrue(LanguageReport.fetch(connection).stream().allMatch(
                        row -> row.speakers().signum() == 0 && row.worldPercentage() == null));
                data.execute("DELETE FROM countrylanguage");
                data.execute("DELETE FROM country");
                var emptyWorld = LanguageReport.fetch(connection);
                assertEquals(5, emptyWorld.size());
                assertTrue(emptyWorld.stream().allMatch(
                        row -> row.speakers().signum() == 0 && row.worldPercentage() == null));
                assertFalse(connection.isClosed());
            } finally {
                connection.setCatalog(originalCatalog);
                setup.execute("DROP DATABASE " + schema);
            }
        }
    }
}
