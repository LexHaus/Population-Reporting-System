package org.example;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LanguageReportTest {
    @Test void displaysRoundedEstimatesAndUndefinedPercentage() {
        var output = new ByteArrayOutputStream();
        LanguageReport.print(List.of(
                new LanguageReport.Row("Arabic", new BigDecimal("233839238.7"), new BigDecimal("3.8468313363")),
                new LanguageReport.Row("Hindi", BigDecimal.ZERO, null)), new PrintStream(output));
        String report = output.toString();
        assertTrue(report.contains("233,839,239"));
        assertTrue(report.contains("3.85"));
        assertTrue(report.contains("N/A"));
        assertTrue(report.contains("World sample dataset"));
    }

    @Test void roundsHalfUpAtDisplayBoundary() {
        var output = new ByteArrayOutputStream();
        LanguageReport.print(List.of(new LanguageReport.Row("Arabic", new BigDecimal("125.5"),
                new BigDecimal("3.125"))), new PrintStream(output));
        assertTrue(output.toString().contains("126"));
        assertTrue(output.toString().contains("3.13"));
    }
}
