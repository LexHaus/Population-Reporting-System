# City Population Reports

This folder contains the city population reports for the Population Reporting System.

## Running from Java

From the nested `Population-Reporting-System` application directory, run `mvn package`.
With `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD` set for your local
World database, run:

```sh
java -jar target/Population-Reporting-System-1.0-SNAPSHOT.jar cities
java -jar target/Population-Reporting-System-1.0-SNAPSHOT.jar cities US08
```

`cities` defaults to US12 (worldwide Top 10). Select US07 through US16 explicitly
to run another existing query. In IntelliJ, run `org.example.Main` with program
arguments `cities US12` and the same database environment variables.
For Docker, rebuild the app first, then run `docker compose run --rm app cities US12`.

The current SQL uses fixed filters: Europe, Western Europe, United States, and
California. Top N reports currently use N=10; user-selectable filters and N remain
future work. These are historical World sample values, not current populations.

Verified against local MySQL on 14 October 2026: all ten SQL queries ran, and the
Java JDBC integration check verified counts, descending order, largest cities,
and matching Top 10 subsets. Six Java tests passed with no skips when
`WORLD_TEST_URL`, `DB_USER`, and `DB_PASSWORD` were supplied to `mvn package`.
See [verification evidence](evidence/).

## US07 - View all cities worldwide by population

### User Story

As a user, I want to view all cities in the world organized by population from largest to smallest, so that I can compare the populations of cities worldwide.

### Acceptance Criteria

- The system displays all cities in the World database.
- Cities are organized from largest population to smallest population.
- The report displays:
    - name
    - country
    - district
    - population

### SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables using the city's country code and orders the results by city population in descending order.

### Expected Output

The report will display the following columns:

| Name | Country | District | Population |
|------|---------|----------|------------|
| City name | Country name | District name | Population |

Results are ordered from the largest city population to the smallest.

### Status

US07 SQL and Java integration verified against the World sample database.

---

## US08 - View all cities in a continent by population

### User Story

As a user, I want to view all cities in a continent organized by population from largest to smallest, so that I can compare the populations of cities within a continent.

### Acceptance Criteria

- The system displays all cities in the selected continent.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

### SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables and filters cities by continent.

### Expected Output

The report will display the following columns:

`name | country | district | population`

The results are ordered by population from largest to smallest.

## US09 - View all cities in a region by population

### User Story

As a user, I want to view all cities in a region organized by population from largest to smallest, so that I can compare the populations of cities within a region.

### Acceptance Criteria

- The system displays all cities in the selected region.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

### SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables and filters cities by region.

### Expected Output

The report will display the following columns:

`name | country | district | population`

The results are ordered by population from largest to smallest.

## US10 - View all cities in a country by population

### User Story

As a user, I want to view all cities in a country organized by population from largest to smallest, so that I can compare the populations of cities within a country.

### Acceptance Criteria

- The system displays all cities in the selected country.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

### SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables and filters cities by country.

### Expected Output

The report will display the following columns:

`name | country | district | population`

The results are ordered by population from largest to smallest.

## US11 - View all cities in a district by population

### User Story

As a user, I want to view all cities in a district organized by population from largest to smallest, so that I can compare the populations of cities within a district.

### Acceptance Criteria

- The system displays all cities in the selected district.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

### SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables and filters cities by district.

### Expected Output

The report will display the following columns:

`name | country | district | population`

The results are ordered by population from largest to smallest.

## US12 - View Top N cities worldwide by population

### User Story

As a user, I want to view the Top N populated cities in the world, so that I can compare the largest cities worldwide.

### Acceptance Criteria

- The user can specify the number N.
- The system displays the Top N populated cities worldwide.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

### SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables, orders cities by population from largest to smallest, and limits the results to the specified number.

### Expected Output

The report will display the following columns:

`name | country | district | population`

The results contain the Top N cities ordered by population from largest to smallest.

# US13 - View Top N cities in a continent by population

## User Story

As a user, I want to view the Top N populated cities in a continent, so that I can compare the largest cities within a selected continent.

## Acceptance Criteria

- The user can specify the number N.
- The system displays the Top N populated cities in the selected continent.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

## SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables, filters cities by continent, orders them by population from largest to smallest, and limits the results to N cities.

## Expected Output

The report will display the following columns:

`name | country | district | population`

The results contain the Top N cities in the selected continent ordered by population from largest to smallest.

# US14 - View Top N cities in a region by population

## User Story

As a user, I want to view the Top N populated cities in a region, so that I can compare the largest cities within a selected region.

## Acceptance Criteria

- The user can specify the number N.
- The system displays the Top N populated cities in the selected region.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

## SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables, filters cities by region, orders them by population from largest to smallest, and limits the results to N cities.

## Expected Output

The report will display the following columns:

`name | country | district | population`

The results contain the Top N cities in the selected region ordered by population from largest to smallest.

# US15 - View Top N cities in a country by population

## User Story

As a user, I want to view the Top N populated cities in a country, so that I can compare the largest cities within a selected country.

## Acceptance Criteria

- The user can specify the number N.
- The system displays the Top N populated cities in the selected country.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

## SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables, filters cities by country, orders them by population from largest to smallest, and limits the results to N cities.

## Expected Output

The report will display the following columns:

`name | country | district | population`

The results contain the Top N cities in the selected country ordered by population from largest to smallest.

# US16 - View Top N cities in a district by population

## User Story

As a user, I want to view the Top N populated cities in a district, so that I can compare the largest cities within a selected district.

## Acceptance Criteria

- The user can specify the number N.
- The system displays the Top N populated cities in the selected district.
- Cities are organized from largest population to smallest population.
- The report displays:
  - name
  - country
  - district
  - population

## SQL Query

The SQL query for this report is located in `report.sql`.

The query joins the `city` and `country` tables, filters cities by district, orders them by population from largest to smallest, and limits the results to N cities.

## Expected Output

The report will display the following columns:

`name | country | district | population`

The results contain the Top N cities in the selected district ordered by population from largest to smallest.
