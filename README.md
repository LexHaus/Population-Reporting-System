# Population-Reporting-System

[![Build: master](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml?query=branch%3Amaster)
[![Build: develop](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml/badge.svg?branch=develop&event=push)](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml?query=branch%3Adevelop)
[![Coverage: master](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2FLexHaus%2FPopulation-Reporting-System%2Fcoverage-badges%2Fmaster.json&cacheSeconds=300)](https://github.com/LexHaus/Population-Reporting-System/tree/coverage-badges)
[![Release](https://img.shields.io/github/v/release/LexHaus/Population-Reporting-System?display_name=release)](https://github.com/LexHaus/Population-Reporting-System/releases/latest)
[![Licence: MIT](https://img.shields.io/github/license/LexHaus/Population-Reporting-System)](LICENSE)

SET9803 DevOps Group Project Assessment

## Reports

- [System use-case diagram and requirement mapping](use-cases/README.md)

- [Language report: query, user story and test instructions](reports/languages/README.md)

## Project Overview

This project is a population reporting system developed as part of the SET9803 DevOps module.

## Group 26 Team
- 40800010 Deleon Esdaille
- 40858935 Affan Nadeem
- 40864161 Alexis Neuhaus
- 40732011 Ugochukwu Hyacinth Okehie Okehie
- 40857660 Alexander Siegel
- 40832973 Auguste Williams

## Tools
- Java
- IntelliJ
- Maven
- Git and GitHub
- Docker

- ## Development

The project follows GitFlow with
- 'master'
- 'develop'
- 'release'

## Verified assessment evidence

The language-report requirement is verified in this release. This table covers
US32 only; it is not a claim that all 32 assessment requirements are complete.

| ID | Name | Met | Screenshot / evidence |
|---|---|---|---|
| US32 | Speakers of Chinese, English, Hindi, Spanish and Arabic, ordered by estimated speakers with world percentages | Yes | [Java output screenshot](reports/languages/evidence/java-report.jpg), [Docker/MySQL output](reports/languages/evidence/docker-mysql-output.txt) |

Verified on 13 October 2026 with Java 17 in Docker and MySQL 8.4.11. The screenshot
shows actual Java stdout presented in a browser during the earlier MariaDB run;
the final Docker/MySQL run produced exactly the same output. Six calculation
tests, two formatting tests and one JDBC integration test passed.

See [application setup](Population-Reporting-System/README.md) and
[language report instructions](reports/languages/README.md). City report SQL and
documentation are also included; this release does not claim their Java integration
or completion of the whole assessment.

## Automated build and tests

[GitHub Actions: Build and test](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml)
 runs on pushes and pull requests. It starts MySQL with the supplied World data,
 runs the six Python calculation tests and Maven's unit and JDBC integration tests,
 builds the executable JAR, and builds the Docker image on Linux using `Dockerfile`.
 It then runs the country, city and language reports inside Docker.

Open a workflow run to see each step and download `build-and-test-evidence`,
which contains the JAR, Maven test results and report output. A successful run is
visible as a green check; integration tests require `WORLD_TEST_URL` and are
explicitly enabled in this workflow.

### Automatic coverage badge

The coverage badge measures **all Java application lines on `master`**, including
unit and MySQL integration tests. It is separate from the language-report-only
coverage and does not include the Python tests.

After a successful push build on `master` (or a manual build of `master`), Actions
publishes the measured JaCoCo result to the `coverage-badges` branch using its
built-in repository token. No external coverage account or personal token is
required. Pull requests and other branches cannot publish the master badge.
Failed builds retain the last successful measurement; the master build badge
shows the build status. Badge caching can delay display updates by a few minutes.

The first coverage value becomes available after this workflow reaches `master`
and passes there. Until then the coverage badge may show unavailable. The
`master-source.json` file on the badge branch records the measured commit, and
`jacoco.xml` contains the underlying coverage counters. Download
`build-and-test-evidence` from the matching run for the full HTML report.
