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

    @Test void emptyReportStillDisplaysColumnHeadings() {
        var output = new ByteArrayOutputStream();
        LanguageReport.print(List.of(), new PrintStream(output));
        String report = output.toString();
        assertEquals(2, report.lines().count());
        assertTrue(report.contains("Estimated speakers"));
        assertTrue(report.contains("World population (%)"));
    }

    @Test void formattingIsIndependentOfDefaultLocaleAndKeepsInputOrder() {
        var previous = java.util.Locale.getDefault();
        try {
            java.util.Locale.setDefault(java.util.Locale.GERMANY);
            var output = new ByteArrayOutputStream();
            LanguageReport.print(List.of(
                    new LanguageReport.Row("English", new BigDecimal("12345678901.49"), new BigDecimal("1.2")),
                    new LanguageReport.Row("Arabic", BigDecimal.ZERO, BigDecimal.ZERO)), new PrintStream(output));
            var lines = output.toString().lines().toList();
            assertEquals(List.of("English", "12,345,678,901", "1.20"), List.of(lines.get(2).trim().split("\\s+")));
            assertEquals(List.of("Arabic", "0", "0.00"), List.of(lines.get(3).trim().split("\\s+")));
        } finally {
            java.util.Locale.setDefault(previous);
        }
    }
}
