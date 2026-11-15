package org.example;

import java.util.*;

/** Validates user inputs before opening a database connection. */
public record ReportRequest(int number, Map<String, String> options) {
    public ReportRequest {
        if (number < 1 || number > 32) throw new IllegalArgumentException("Report must be US01 through US32");
        options = Map.copyOf(options);
        Set<String> allowed = new HashSet<>();
        String scope = scope(number);
        if (!scope.equals("world")) allowed.add(scope);
        if (scope.equals("district")) allowed.add("country");
        if (isTop(number)) allowed.add("top");
        if (!allowed.containsAll(options.keySet())) throw new IllegalArgumentException("Unsupported options for " + id(number));
        for (String name : allowed) {
            if (!options.containsKey(name) || options.get(name).isBlank())
                throw new IllegalArgumentException("Required option: --" + name);
        }
        if (isTop(number)) positive(options.get("top"), "top");
        if (number == 31) positive(options.get("city-id"), "city-id");
    }

    public static ReportRequest parse(String[] args) {
        if (args.length == 0 || !args[0].matches("US(0[1-9]|[12][0-9]|3[0-2])"))
            throw new IllegalArgumentException("Report must be US01 through US32");
        Map<String, String> options = new HashMap<>();
        for (int i = 1; i < args.length; i += 2) {
            if (!args[i].startsWith("--") || i + 1 >= args.length)
                throw new IllegalArgumentException("Options require --name value pairs");
            if (options.putIfAbsent(args[i].substring(2), args[i + 1]) != null)
                throw new IllegalArgumentException("Duplicate option: " + args[i]);
        }
        return new ReportRequest(Integer.parseInt(args[0].substring(2)), options);
    }

    static int positive(String value, String name) {
        try {
            int result = Integer.parseInt(value);
            if (result > 0) return result;
        } catch (NumberFormatException ignored) { }
        throw new IllegalArgumentException("--" + name + " must be a positive integer");
    }

    public static String id(int number) { return String.format(Locale.ROOT, "US%02d", number); }
    public boolean top() { return isTop(number); }
    private static boolean isTop(int n) { return n >= 4 && n <= 6 || n >= 12 && n <= 16 || n >= 20 && n <= 22; }
    public String scope() { return scope(number); }
    private static String scope(int n) {
        if (n <= 6) return List.of("world", "continent", "region").get((n - 1) % 3);
        if (n <= 16) return List.of("world", "continent", "region", "country", "district").get((n - 7) % 5);
        if (n <= 22) return List.of("world", "continent", "region").get((n - 17) % 3);
        if (n <= 26 || n == 32) return "world";
        return List.of("continent", "region", "country", "district", "city-id").get(n - 27);
    }
}
