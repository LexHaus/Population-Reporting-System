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
| US32 | Speakers of Chinese, English, Hindi, Spanish and Arabic, ordered by estimated speakers with world percentages | Yes | [Final Docker output screenshot](reports/languages/evidence/final/language-report.jpg), [deployment and tests](reports/languages/evidence/final/deployment-verification.jpg), [verification details](reports/languages/evidence/final/README.md) |

Verified again on 15 November 2026 from a fresh Docker Compose database volume,
using the Java 17 application container and MySQL 8.4.11. Nine Java tests passed
with no skips, including language and city JDBC integration tests; six Python
calculation tests also passed. LanguageReport line coverage is 100% (20/20),
with 87.5% branch coverage (7/8). The screenshots display captured command output
in an explicitly labelled evidence viewer and the actual JaCoCo report.

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
