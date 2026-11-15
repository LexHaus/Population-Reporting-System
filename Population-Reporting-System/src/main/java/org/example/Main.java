package org.example;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class Main {
    static final String USAGE = "Usage: java -jar app.jar report US01..US32 [--continent value | --region value | --country value | --district value --country value | --city-id ID] [--top N]\n"
            + "Aliases: countries [US01..US06], cities [US07..US16], capitals [US17..US22], population [US23..US31], languages. See reports/README.md for examples.";

    @FunctionalInterface
    interface ConnectionFactory { Connection open() throws Exception; }

    public static void main(String[] args) {
        int status = run(args, System.out, System.err, () -> {
            var env = System.getenv();
            String url = "jdbc:mysql://" + env.getOrDefault("DB_HOST", "localhost") + ":"
                    + env.getOrDefault("DB_PORT", "3306") + "/" + env.getOrDefault("DB_NAME", "world");
            return DriverManager.getConnection(url, env.getOrDefault("DB_USER", "root"), env.getOrDefault("DB_PASSWORD", ""));
        });
        if (status != 0) System.exit(status);
    }

    static int run(String[] args, PrintStream out, PrintStream err, ConnectionFactory connections) {
        if (args.length == 1 && (args[0].equals("--help") || args[0].equals("help"))) {
            out.println(USAGE);
            return 0;
        }
        ReportRequest request;
        try { request = request(args); }
        catch (IllegalArgumentException e) {
            err.println(e.getMessage()); err.println(USAGE); return 2;
        }
        try (var connection = connections.open()) {
            if (request.number() == 32) LanguageReport.print(LanguageReport.fetch(connection), out);
            else PopulationReports.print(PopulationReports.fetch(connection, request), request, out);
            return 0;
        } catch (Exception e) {
            err.println("Could not generate the report from the World database: " + e.getMessage());
            return 1;
        }
    }

    static ReportRequest request(String[] args) {
        if (args.length == 0) return ReportRequest.parse(new String[]{"US04", "--top", "10"});
        if (args[0].equals("report")) return ReportRequest.parse(Arrays.copyOfRange(args, 1, args.length));
        if (args[0].equals("languages")) {
            if (args.length != 1) throw new IllegalArgumentException("languages takes no options");
            return new ReportRequest(32, Map.of());
        }
        int min, max, fallback;
        switch (args[0]) {
            case "countries" -> { min=1; max=6; fallback=4; }
            case "cities" -> { min=7; max=16; fallback=12; }
            case "capitals" -> { min=17; max=22; fallback=20; }
            case "population" -> { min=23; max=31; fallback=26; }
            default -> throw new IllegalArgumentException("Unknown report command: " + args[0]);
        }
        String id = args.length >= 2 ? args[1] : ReportRequest.id(fallback);
        var input = new ArrayList<String>(); input.add(id);
        input.addAll(Arrays.asList(args).subList(Math.min(2, args.length), args.length));
        // Preserve existing short demo commands. The explicit `report` command
        // always requires user-supplied filters and N for parameterised reports.
        if (input.size() == 1 && id.matches("US(0[1-9]|[12][0-9]|3[0-2])")) {
            int n = Integer.parseInt(id.substring(2));
            String scope = n <= 6 ? List.of("world","continent","region").get((n-1)%3)
                    : n <= 16 ? List.of("world","continent","region","country","district").get((n-7)%5)
                    : n <= 22 ? List.of("world","continent","region").get((n-17)%3) : "world";
            if (!scope.equals("world")) {
                input.add("--" + scope);
                input.add(switch(scope) { case "continent" -> "Europe"; case "region" -> "Western Europe"; case "country" -> "United States"; default -> "California"; });
                if (scope.equals("district")) { input.add("--country"); input.add("USA"); }
            }
            if (n>=4 && n<=6 || n>=12 && n<=16 || n>=20 && n<=22) { input.add("--top"); input.add("10"); }
        }
        ReportRequest request = ReportRequest.parse(input.toArray(String[]::new));
        if (request.number() < min || request.number() > max)
            throw new IllegalArgumentException("Report ID does not belong to " + args[0]);
        return request;
    }
}
