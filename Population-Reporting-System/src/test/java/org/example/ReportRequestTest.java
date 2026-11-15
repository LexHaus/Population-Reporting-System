package org.example;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ReportRequestTest {
    @Test void rejectsInvalidIdsMissingFiltersAndInvalidTop() {
        for (String[] input : List.of(new String[]{"US00"}, new String[]{"US33"}, new String[]{"US4"},
                new String[]{"US02"}, new String[]{"US04","--top","0"}, new String[]{"US04","--top","-1"},
                new String[]{"US04","--top","2147483648"}, new String[]{"US04","--top","abc"},
                new String[]{"US04","--top"}, new String[]{"US01","--top","2"},
                new String[]{"US04","--top","2","--top","3"}, new String[]{"US31","--city-id","0"},
                new String[]{"US30","--district","California"}, new String[]{"US02","--continent"," "}))
            assertThrows(IllegalArgumentException.class, () -> ReportRequest.parse(input), Arrays.toString(input));
    }

    @Test void treatsLocationsAsBoundValues() {
        String malicious = "Europe' OR 1=1 --";
        var request = ReportRequest.parse(new String[]{"US05","--continent",malicious,"--top","3"});
        var query = PopulationReports.query(request);
        assertFalse(query.sql().contains(malicious));
        assertEquals(List.of(malicious,3),query.values());
    }

    @Test void cliRejectsErrorsBeforeConnectingAndSupportsHelp() {
        var bytes = new ByteArrayOutputStream(); var stream = new PrintStream(bytes);
        assertEquals(2,Main.run(new String[]{"report","US04","--top","0"},stream,stream, () -> { fail("Must not connect"); return null; }));
        assertEquals(0,Main.run(new String[]{"--help"},stream,stream, () -> { fail("Must not connect"); return null; }));
        assertEquals(1,Main.run(new String[]{"report","US01"},stream,stream, () -> { throw new java.sql.SQLException("unavailable"); }));
        assertTrue(bytes.toString().contains("unavailable"));
    }

    @Test void preservesDemoAliasesAndChecksReportFamilies() {
        assertEquals(4,Main.request(new String[]{}).number());
        assertEquals("10",Main.request(new String[]{"countries"}).options().get("top"));
        assertEquals("USA",Main.request(new String[]{"cities","US16"}).options().get("country"));
        assertEquals(20,Main.request(new String[]{"capitals"}).number());
        assertEquals(26,Main.request(new String[]{"population"}).number());
        assertEquals(32,Main.request(new String[]{"languages"}).number());
        assertThrows(IllegalArgumentException.class, () -> Main.request(new String[]{"cities","US01"}));
        assertThrows(IllegalArgumentException.class, () -> Main.request(new String[]{"languages","extra"}));
        assertThrows(IllegalArgumentException.class, () -> Main.request(new String[]{"unknown"}));
    }

    @Test void emptyReportsKeepHeadings() {
        var bytes = new ByteArrayOutputStream();
        PopulationReports.print(new PopulationReports.Table(List.of("Name","Population"),List.of()),
                new ReportRequest(1,Map.of()),new PrintStream(bytes));
        assertTrue(bytes.toString().contains("No matching records."));
    }
}
