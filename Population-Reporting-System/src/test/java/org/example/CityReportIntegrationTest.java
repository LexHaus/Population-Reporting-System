package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.sql.DriverManager;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named = "WORLD_TEST_URL", matches = ".+")
class CityReportIntegrationTest {
    @Test void verifiesAllTenReportsAgainstWorldSample() throws Exception {
        try (var connection = DriverManager.getConnection(System.getenv("WORLD_TEST_URL"),
                System.getenv().getOrDefault("DB_USER", "root"), System.getenv("DB_PASSWORD"))) {
            int[] counts = {4079, 841, 186, 274, 68};
            String[] largest = {"Mumbai (Bombay)", "Moscow", "Berlin", "New York", "Los Angeles"};
            for (int i = 0; i < 5; i++) {
                var rows = CityReport.fetch(connection, String.format("US%02d", i + 7));
                assertEquals(counts[i], rows.size());
                assertEquals(largest[i], rows.get(0).name());
                for (int j = 1; j < rows.size(); j++) {
                    assertTrue(rows.get(j - 1).population() >= rows.get(j).population());
                }
                assertEquals(rows.subList(0, 10), CityReport.fetch(connection, "US" + (i + 12)));
            }
            assertFalse(connection.isClosed());
        }
    }
}
