# City Population Reports

This folder contains the city population reports for the Population Reporting System.

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

US07 implementation started. SQL query prepared. Database testing and application integration are still pending.

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