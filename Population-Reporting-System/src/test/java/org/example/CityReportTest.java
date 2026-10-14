package org.example;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CityReportTest {
    @Test void rejectsInvalidReportIds() {
        for (String id : List.of("US06", "US17", "US7", "US12; DROP TABLE city", "")) {
            assertThrows(IllegalArgumentException.class, () -> CityReport.reportNumber(id));
        }
    }
    @Test void formatsAllRequiredColumnsAndEmptyReports() {
        var bytes = new ByteArrayOutputStream();
        var out = new PrintStream(bytes);
        CityReport.print(List.of(new CityReport.Row("New York", "United States", "New York", 8008278)), "US15", out);
        String text = bytes.toString();
        for (String value : List.of("Name", "Country", "District", "Population", "New York", "United States", "8,008,278", "Rows: 1")) {
            assertTrue(text.contains(value), value);
        }
        bytes.reset();
        CityReport.print(List.of(), "US15", out);
        assertTrue(bytes.toString().contains("Rows: 0"));
    }
}
