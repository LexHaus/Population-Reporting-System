package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named="WORLD_TEST_URL", matches=".+")
class PopulationReportsIntegrationTest {
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(System.getenv("WORLD_TEST_URL"),
                System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""));
    }
    static ReportRequest request(int n) {
        var options = new HashMap<String,String>();
        if (List.of(2,5,8,13,18,21,27).contains(n)) options.put("continent","Asia");
        if (List.of(3,6,9,14,19,22,28).contains(n)) options.put("region","Eastern Asia");
        if (List.of(10,15,29).contains(n)) options.put("country","JPN");
        if (List.of(11,16,30).contains(n)) { options.put("district","Tokyo-to"); options.put("country","JPN"); }
        if (List.of(4,5,6,12,13,14,15,16,20,21,22).contains(n)) options.put("top","3");
        if (n==31) options.put("city-id","1532");
        return new ReportRequest(n,options);
    }

    @TestFactory Collection<DynamicTest> verifiesEveryCountryCityCapitalAndPopulationRequirement() {
        var tests = new ArrayList<DynamicTest>();
        for (int number=1; number<=31; number++) {
            int n=number;
            tests.add(DynamicTest.dynamicTest(ReportRequest.id(n), () -> {
                try (var connection=connect()) {
                    var table=PopulationReports.fetch(connection,request(n));
                    assertFalse(table.rows().isEmpty(),"World fixture must match " + ReportRequest.id(n));
                    int columns=n<=6 ? 6 : n<=16 ? 4 : n<=22 ? 3 : n<=25 ? 6 : n==30 ? 3 : n==31 ? 5 : 2;
                    assertEquals(columns,table.headings().size());
                    if (n<=22) {
                        int pop=table.headings().indexOf("Population");
                        for (int i=1;i<table.rows().size();i++)
                            assertTrue(Long.parseLong(table.rows().get(i-1).get(pop))>=Long.parseLong(table.rows().get(i).get(pop)));
                        if (request(n).top()) assertEquals(3,table.rows().size());
                    }
                    if (n<=22 && !request(n).scope().equals("world")) {
                        for (var row:table.rows()) {
                            String country=row.get(1);
                            try (var lookup=connection.prepareStatement("SELECT Code,Continent,Region FROM country WHERE Name=?")) {
                                lookup.setString(1,country);
                                try(var result=lookup.executeQuery()) {
                                    assertTrue(result.next());
                                    switch(request(n).scope()) {
                                        case "continent" -> assertEquals("Asia",result.getString("Continent"));
                                        case "region" -> assertEquals("Eastern Asia",result.getString("Region"));
                                        default -> assertEquals("JPN",result.getString("Code"));
                                    }
                                }
                            }
                            if(request(n).scope().equals("district")) assertEquals("Tokyo-to",row.get(2));
                        }
                    }
                    if (n>=23 && n<=25) {
                        long total=0;
                        for (var row:table.rows()) {
                            long population=Long.parseLong(row.get(1));
                            assertEquals(population,Long.parseLong(row.get(2))+Long.parseLong(row.get(4)));
                            total+=population;
                        }
                        assertEquals(6078749450L,total,"Each grouping must preserve World total");
                    }
                    if (n==26) assertEquals("6078749450",table.rows().get(0).get(1));
                    if (n==29) assertEquals(List.of("Japan","126714000"),table.rows().get(0));
                    if (n==31) assertEquals("Tokyo",table.rows().get(0).get(1));
                    assertFalse(connection.isClosed());
                }
            }));
        }
        return tests;
    }

    @Test void filtersAndTopValuesMatchIndependentWorldQueries() throws Exception {
        try (var connection=connect(); var statement=connection.createStatement()) {
            var all=PopulationReports.fetch(connection,request(1));
            assertEquals(239,all.rows().size());
            assertEquals(List.of("Code","Name","Continent","Region","Population","Capital"),all.headings());
            try(var expected=statement.executeQuery("SELECT c.Code,c.Name,c.Continent,c.Region,c.Population,city.Name FROM country c LEFT JOIN city ON city.ID=c.Capital ORDER BY c.Population DESC,c.Name,c.Code")) {
                int i=0;
                while(expected.next()) {
                    var row=all.rows().get(i++);
                    for(int col=1;col<=6;col++) assertEquals(Objects.toString(expected.getString(col),"N/A"),row.get(col-1));
                }
                assertEquals(239,i);
            }
            for(int top:List.of(1,7,10000)) {
                var subset=PopulationReports.fetch(connection,new ReportRequest(4,Map.of("top",String.valueOf(top))));
                assertEquals(all.rows().subList(0,Math.min(top,239)),subset.rows());
            }
            for (int[] pair : List.of(new int[]{7,12},new int[]{17,20})) {
                var full=PopulationReports.fetch(connection,new ReportRequest(pair[0],Map.of()));
                for(int top:List.of(1,7,10000)) {
                    var subset=PopulationReports.fetch(connection,new ReportRequest(pair[1],Map.of("top",String.valueOf(top))));
                    assertEquals(full.rows().subList(0,Math.min(top,full.rows().size())),subset.rows());
                }
            }
            var countries=PopulationReports.fetch(connection,request(2));
            assertTrue(countries.rows().stream().allMatch(row->row.get(2).equals("Asia")));
            var cities=PopulationReports.fetch(connection,request(10));
            assertTrue(cities.rows().stream().allMatch(row->row.get(1).equals("Japan")));
            var injected=PopulationReports.fetch(connection,new ReportRequest(2,Map.of("continent","Asia' OR 1=1 --")));
            assertTrue(injected.rows().isEmpty());
            assertTrue(PopulationReports.fetch(connection,new ReportRequest(31,Map.of("city-id","2147483647"))).rows().isEmpty());
        }
    }

    @Test void syntheticDataVerifiesAggregationMissingCapitalsAndDistrictDisambiguation() throws Exception {
        try(var connection=connect(); var admin=connection.createStatement()) {
            String original=connection.getCatalog();
            String schema="population_test_"+UUID.randomUUID().toString().replace("-","");
            admin.execute("CREATE DATABASE "+schema);
            connection.setCatalog(schema);
            try(var data=connection.createStatement()) {
                data.execute("CREATE TABLE country (Code CHAR(3),Name VARCHAR(50),Continent VARCHAR(50),Region VARCHAR(50),Population BIGINT,Capital INT)");
                data.execute("CREATE TABLE city (ID INT,Name VARCHAR(50),CountryCode CHAR(3),District VARCHAR(50),Population BIGINT)");
                data.execute("INSERT INTO country VALUES ('AAA','Alpha','Asia','East',1000,1),('BBB','Beta','Asia','East',500,3),('CCC','Gamma','Europe','West',0,NULL),('DDD','Delta','Europe','West',500,NULL)");
                data.execute("INSERT INTO city VALUES (1,'A1','AAA','D1',100),(2,'A2','AAA','D1',150),(3,'B1','BBB','D1',200),(4,'B2','BBB','D2',50)");
                var continent=PopulationReports.fetch(connection,new ReportRequest(23,Map.of()));
                assertEquals(List.of("Asia","1500","500","33.33","1000","66.67"),continent.rows().get(0));
                assertEquals(List.of("Europe","500","0","0.00","500","100.00"),continent.rows().get(1));
                var countries=PopulationReports.fetch(connection,new ReportRequest(25,Map.of()));
                assertTrue(countries.rows().contains(List.of("Gamma","0","0","N/A","0","N/A")));
                var district=PopulationReports.fetch(connection,new ReportRequest(30,Map.of("district","D1","country","AAA")));
                assertEquals(List.of(List.of("D1","Alpha","250")),district.rows());
                var capitals=PopulationReports.fetch(connection,new ReportRequest(17,Map.of()));
                assertEquals(List.of(List.of("B1","Beta","200"),List.of("A1","Alpha","100")),capitals.rows());
                var all=PopulationReports.fetch(connection,new ReportRequest(1,Map.of()));
                assertTrue(all.rows().stream().anyMatch(row->row.get(1).equals("Delta") && row.get(5).equals("N/A")));
                data.execute("DELETE FROM city");
                data.execute("DELETE FROM country");
                assertEquals(List.of(List.of("World","0")),PopulationReports.fetch(connection,new ReportRequest(26,Map.of())).rows());
                assertTrue(PopulationReports.fetch(connection,new ReportRequest(23,Map.of())).rows().isEmpty());
            } finally {
                connection.setCatalog(original);
                admin.execute("DROP DATABASE "+schema);
            }
        }
    }

    @Test void cliRunsReportsAndClosesItsConnection() throws Exception {
        var output=new java.io.ByteArrayOutputStream();
        var stream=new java.io.PrintStream(output);
        var connection=connect();
        assertEquals(0,Main.run(new String[]{"report","US26"},stream,stream,()->connection));
        assertTrue(connection.isClosed());
        assertTrue(output.toString().contains("6078749450"));
        assertEquals(0,Main.run(new String[]{"languages"},stream,stream,this::connect));
        assertTrue(output.toString().contains("Chinese"));
    }
}
