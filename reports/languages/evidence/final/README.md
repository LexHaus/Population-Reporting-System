# Final language-report deployment verification

Verified 15 November 2026 against source commit
`af8357024c650cf4ee69cc04eded579676a1a501` on `develop`.

A separate Compose project (`language-final-verification`) created a fresh
`world-data` volume, imported the supplied World SQL and built the Java 17 image
from the packaged JAR. MySQL 8.4.11 became healthy. The application container ran
`languages` successfully (exit 0) over the Compose network using `database:3306`.
Host port 33308 was used only for JDBC test access to avoid existing services.
This verifies a local Docker deployment; it does not claim a hosted service.

## Evidence

- [Language report screenshot](language-report.jpg) and [unchanged stdout](language-report.txt).
- [Deployment/test screenshot](deployment-verification.jpg), [database state](deployment-status.txt), and [database counts/version](database-verification.txt).
- [Full Maven output](maven-verification.txt): 9 tests, no failures/errors/skips.
- [Python output](python-verification.txt): 6 tests passed.
- [Actual JaCoCo screenshot](language-coverage.jpg), [XML](jacoco.xml), [CSV](jacoco.csv), and [complete HTML coverage report ZIP](coverage-report.zip).

LanguageReport: 20/20 lines (100%), 7/8 branches (87.5%). JaCoCo displays
instruction coverage as 95% and branch coverage rounded down to 87%; these
are different counters from line coverage. Whole-application Java line coverage
is 49/91 (53.8%). The SQL edge-case checks run in a separate disposable schema.

The language and deployment screenshots are browser captures of clearly labelled
HTML evidence viewers built from saved stdout/test logs. They are not an
application web UI or IntelliJ screenshots. The coverage screenshot is the actual
JaCoCo HTML report. Data represents historical World sample estimates.

## Reproduce

From the repository root, create an ignored environment file with your own
`DB_PASSWORD`, then build and start the database:

```sh
mvn -f Population-Reporting-System/pom.xml clean package
export DB_PASSWORD='your-local-test-password'
docker compose -f Population-Reporting-System/compose.yaml \
  -f reports/languages/evidence/final/compose-verification.override.yaml \
  -p language-final-verification build app
docker compose -f Population-Reporting-System/compose.yaml \
  -f reports/languages/evidence/final/compose-verification.override.yaml \
  -p language-final-verification up -d --wait database
docker compose -f Population-Reporting-System/compose.yaml \
  -f reports/languages/evidence/final/compose-verification.override.yaml \
  -p language-final-verification run --rm --no-deps app languages
WORLD_TEST_URL=jdbc:mysql://127.0.0.1:33308/world DB_USER=root \
  mvn -f Population-Reporting-System/pom.xml clean verify
python3 -m unittest discover -s tests/languages -v
```

Keep the exported password in the same shell for integration tests. The MySQL
integration test account must be able to create/drop its isolated test schema.
The report is a command-line task: successful exit after printing is expected.
Wait for the database with `--wait`; run the app as a one-off command.

After recording evidence, remove only this disposable project's resources:

```sh
docker compose -f Population-Reporting-System/compose.yaml \
  -f reports/languages/evidence/final/compose-verification.override.yaml \
  -p language-final-verification down --volumes
```
