# Language population report

Status: Implemented, tested and integrated into develop. Final verification on
13 October 2026 used the Java 17 application container and MySQL 8.4.11. All five
results exactly matched the saved Java output; all nine tests passed.

## User story

As a population analyst, I want to compare the estimated number of speakers of
Chinese, English, Hindi, Spanish and Arabic, ordered from largest to smallest,
so that I can understand their relative global usage.

## Acceptance criteria

- Show exactly the five requested languages.
- Sum country population × language percentage ÷ 100 for each language.
- Include both official and unofficial language entries.
- Divide each speaker estimate by the population of all countries, then multiply
  by 100 to obtain its percentage of the world population.
- Sort by unrounded speaker estimate descending; break ties alphabetically.
- Display language, estimated speakers and world population percentage.
- Keep precision during calculation. In the application, display speaker estimates
  rounded to whole people and percentages to two decimal places.
- Show zero speakers for a missing language. If world population is zero or
  unavailable, display the SQL NULL percentage as N/A.
- Label results as estimates from the supplied dataset, not current statistics.

## Query and expected output

`report.sql` uses the World schema's lowercase `country` and `countrylanguage`
tables. Confirm table-name casing in the team's database before integration;
some MySQL installations treat table names as case-sensitive.

Run against an already loaded database with a read-only account:

```sh
mysql -u YOUR_USER -p world < reports/languages/report.sql
```

The synthetic fixture has a total population of 4,000. Its expected display is:

| Language | Estimated speakers | World population (%) |
|---|---:|---:|
| English | 1,200 | 30.00 |
| Chinese | 700 | 17.50 |
| Spanish | 450 | 11.25 |
| Hindi | 200 | 5.00 |
| Arabic | 125 | 3.13 |

This table is test data, not assessment evidence or real World database results.
The French-only country contributes 1,000 people to the denominator. Excluding
it would incorrectly inflate every percentage. Raw Arabic percentage is 3.125.

## Tests without Maven

### World dataset verification

Downloaded from https://downloads.mysql.com/docs/world-db.tar.gz, linked by the
assessment's MySQL example-databases page. Imported into an isolated local server
with networking disabled. No shared team database was modified.

- Country rows: 239; language entries: 984.
- Total world population used as denominator: 6,078,749,450.
- The unmodified query also succeeded with `ONLY_FULL_GROUP_BY` and strict SQL mode.
- Raw database output: [world-results.tsv](world-results.tsv).

| Language | Estimated speakers (rounded) | World population (%) |
|---|---:|---:|
| Chinese | 1,191,843,539 | 19.61 |
| Hindi | 405,633,070 | 6.67 |
| Spanish | 355,029,462 | 5.84 |
| English | 347,077,867 | 5.71 |
| Arabic | 233,839,239 | 3.85 |

These are estimates from the historical sample dataset, not current statistics.
This verifies SQL execution on MariaDB, not Java application output or the team's
exact MySQL version. The TSV retains unrounded values for later comparison.

### Synthetic calculation tests

From the repository root:

```sh
python3 -m unittest discover -s tests/languages -v
```

These tests run the actual query in SQLite against a small synthetic dataset.
They cover filtering, ordering, cross-country aggregation, unofficial languages,
the full-world denominator, missing languages, empty/zero populations,
fractional precision and deterministic ties. SQLite is a calculation check;
run the query on the team's MySQL server as an integration check as well.

`tests/languages/fixture.sql` can also be loaded into a new, disposable MySQL
test database. It creates tables: do not load it into the real World database.

## Sprint tasks and handoff

- [x] Define the user story and acceptance criteria.
- [x] Prepare the report query and synthetic test cases.
- [x] Confirm Java version, package layout and shared database connection with the team.
- [x] Execute the query against the team's supplied World database.
- [x] Add a Java result model and repository method using the shared connection.
- [x] Format console output and handle database failures clearly.
- [x] Add Java/MySQL integration tests to the shared Maven build.
- [x] Capture actual application output and update the assessment evidence table.
- [x] Review and merge into develop, following the team's release process.

Suggested sprint goal: demonstrate an accurate language report from the shared
World database. Tracked in GitHub issue #19 (US32).

## Running the Java report

The application lives in the nested `Population-Reporting-System` directory,
matching the team's DevOps branch. From the repository root:

```sh
mvn -f Population-Reporting-System/pom.xml package
java -jar Population-Reporting-System/target/Population-Reporting-System-1.0-SNAPSHOT.jar languages
```

Set `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` and `DB_PASSWORD` for your database.
The existing default/countries option still prints the starter country report.
The language SQL is packaged from `reports/languages/report.sql`, so there is one
query source. Decimal precision is retained until display; absent percentages
print as N/A. Database/report errors exit with code 1; invalid arguments with 2.

With the team's Compose database already ready, build the JAR and app image, then
run `docker compose run --rm app languages` from the nested application directory.
The Docker/MySQL 8.4 path passed on 13 October 2026 (server 8.4.11).
The host port was overridden to `127.0.0.1:33306:3306` because port 3306 was
occupied; app-to-database networking and the repository images were unchanged.

Two Java formatting tests run in the normal Maven build. To also run the read-only
integration test against the supplied, unmodified World dataset, set
`WORLD_TEST_URL` to its JDBC URL along with `DB_USER` and `DB_PASSWORD`, then run
`mvn -f Population-Reporting-System/pom.xml test`. It checks all five estimates,
their ordering, percentages and that the shared connection stays open.

Actual packaged Java output is saved in [java-world-output.txt](java-world-output.txt).
The [screenshot](evidence/java-report.jpg) records the earlier MariaDB run.
The [final Docker/MySQL output](evidence/docker-mysql-output.txt) matches it exactly.
The root README contains the verified language requirement and evidence links.
