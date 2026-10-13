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
}
