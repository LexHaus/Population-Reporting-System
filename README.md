# Population-Reporting-System

[![Build: master](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml/badge.svg?branch=master&event=push)](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml?query=branch%3Amaster)
[![Build: develop](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml/badge.svg?branch=develop&event=push)](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml?query=branch%3Adevelop)
[![Coverage: master](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2FLexHaus%2FPopulation-Reporting-System%2Fcoverage-badges%2Fmaster.json&cacheSeconds=300)](https://github.com/LexHaus/Population-Reporting-System/tree/coverage-badges)
[![Release](https://img.shields.io/github/v/release/LexHaus/Population-Reporting-System?display_name=release)](https://github.com/LexHaus/Population-Reporting-System/releases/latest)
[![Licence: MIT](https://img.shields.io/github/license/LexHaus/Population-Reporting-System)](LICENSE)

SET9803 DevOps Group Project Assessment

## Reports

- [All 32 report commands, input options and verification output](reports/README.md)

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

**32 requirements of 32 have been implemented and verified, which is 100%.**
This refers to the 32 functional report requirements, not the overall coursework
mark or completion of every submission task.

Verified on 15 November 2026 using source commit
`105ac1c30a918e1852a1044f46e3f7b31bd36a19` merged into `develop` via
[PR #39](https://github.com/LexHaus/Population-Reporting-System/pull/39).
The [post-merge GitHub Actions run passed](https://github.com/LexHaus/Population-Reporting-System/actions/runs/43786628256).
All 48 Java tests passed without skips; all 32 report commands ran successfully
in a Java 17 Docker image against a fresh MySQL 8.4.11 World database.

Screenshots below are browser captures of clearly labelled viewers of actual
Docker stdout. Tab-separated columns are formatted as tables; long results show
the first 12 rows and link to complete unmodified stdout. These are evidence
viewers, not an application web interface. Names and Top N are supplied as command
arguments. See the [evidence pack](reports/final-evidence/README.md) for exact
commands, provenance, logs and checksums.

| ID | Name | Met | Screenshot |
|---|---|---|---|
| US01 | All countries worldwide | Yes | <a href="reports/final-evidence/screenshots/US01.jpg"><img src="reports/final-evidence/screenshots/US01.jpg" width="300" alt="US01 verified report output"></a> · [Full output](reports/final-evidence/raw/US01.txt) |
| US02 | All countries in a continent | Yes | <a href="reports/final-evidence/screenshots/US02.jpg"><img src="reports/final-evidence/screenshots/US02.jpg" width="300" alt="US02 verified report output"></a> · [Full output](reports/final-evidence/raw/US02.txt) |
| US03 | All countries in a region | Yes | <a href="reports/final-evidence/screenshots/US03.jpg"><img src="reports/final-evidence/screenshots/US03.jpg" width="300" alt="US03 verified report output"></a> · [Full output](reports/final-evidence/raw/US03.txt) |
| US04 | Top N countries worldwide | Yes | <a href="reports/final-evidence/screenshots/US04.jpg"><img src="reports/final-evidence/screenshots/US04.jpg" width="300" alt="US04 verified report output"></a> · [Full output](reports/final-evidence/raw/US04.txt) |
| US05 | Top N countries in a continent | Yes | <a href="reports/final-evidence/screenshots/US05.jpg"><img src="reports/final-evidence/screenshots/US05.jpg" width="300" alt="US05 verified report output"></a> · [Full output](reports/final-evidence/raw/US05.txt) |
| US06 | Top N countries in a region | Yes | <a href="reports/final-evidence/screenshots/US06.jpg"><img src="reports/final-evidence/screenshots/US06.jpg" width="300" alt="US06 verified report output"></a> · [Full output](reports/final-evidence/raw/US06.txt) |
| US07 | All cities worldwide | Yes | <a href="reports/final-evidence/screenshots/US07.jpg"><img src="reports/final-evidence/screenshots/US07.jpg" width="300" alt="US07 verified report output"></a> · [Full output](reports/final-evidence/raw/US07.txt) |
| US08 | All cities in a continent | Yes | <a href="reports/final-evidence/screenshots/US08.jpg"><img src="reports/final-evidence/screenshots/US08.jpg" width="300" alt="US08 verified report output"></a> · [Full output](reports/final-evidence/raw/US08.txt) |
| US09 | All cities in a region | Yes | <a href="reports/final-evidence/screenshots/US09.jpg"><img src="reports/final-evidence/screenshots/US09.jpg" width="300" alt="US09 verified report output"></a> · [Full output](reports/final-evidence/raw/US09.txt) |
| US10 | All cities in a country | Yes | <a href="reports/final-evidence/screenshots/US10.jpg"><img src="reports/final-evidence/screenshots/US10.jpg" width="300" alt="US10 verified report output"></a> · [Full output](reports/final-evidence/raw/US10.txt) |
| US11 | All cities in a district | Yes | <a href="reports/final-evidence/screenshots/US11.jpg"><img src="reports/final-evidence/screenshots/US11.jpg" width="300" alt="US11 verified report output"></a> · [Full output](reports/final-evidence/raw/US11.txt) |
| US12 | Top N cities worldwide | Yes | <a href="reports/final-evidence/screenshots/US12.jpg"><img src="reports/final-evidence/screenshots/US12.jpg" width="300" alt="US12 verified report output"></a> · [Full output](reports/final-evidence/raw/US12.txt) |
| US13 | Top N cities in a continent | Yes | <a href="reports/final-evidence/screenshots/US13.jpg"><img src="reports/final-evidence/screenshots/US13.jpg" width="300" alt="US13 verified report output"></a> · [Full output](reports/final-evidence/raw/US13.txt) |
| US14 | Top N cities in a region | Yes | <a href="reports/final-evidence/screenshots/US14.jpg"><img src="reports/final-evidence/screenshots/US14.jpg" width="300" alt="US14 verified report output"></a> · [Full output](reports/final-evidence/raw/US14.txt) |
| US15 | Top N cities in a country | Yes | <a href="reports/final-evidence/screenshots/US15.jpg"><img src="reports/final-evidence/screenshots/US15.jpg" width="300" alt="US15 verified report output"></a> · [Full output](reports/final-evidence/raw/US15.txt) |
| US16 | Top N cities in a district | Yes | <a href="reports/final-evidence/screenshots/US16.jpg"><img src="reports/final-evidence/screenshots/US16.jpg" width="300" alt="US16 verified report output"></a> · [Full output](reports/final-evidence/raw/US16.txt) |
| US17 | All capitals worldwide | Yes | <a href="reports/final-evidence/screenshots/US17.jpg"><img src="reports/final-evidence/screenshots/US17.jpg" width="300" alt="US17 verified report output"></a> · [Full output](reports/final-evidence/raw/US17.txt) |
| US18 | All capitals in a continent | Yes | <a href="reports/final-evidence/screenshots/US18.jpg"><img src="reports/final-evidence/screenshots/US18.jpg" width="300" alt="US18 verified report output"></a> · [Full output](reports/final-evidence/raw/US18.txt) |
| US19 | All capitals in a region | Yes | <a href="reports/final-evidence/screenshots/US19.jpg"><img src="reports/final-evidence/screenshots/US19.jpg" width="300" alt="US19 verified report output"></a> · [Full output](reports/final-evidence/raw/US19.txt) |
| US20 | Top N capitals worldwide | Yes | <a href="reports/final-evidence/screenshots/US20.jpg"><img src="reports/final-evidence/screenshots/US20.jpg" width="300" alt="US20 verified report output"></a> · [Full output](reports/final-evidence/raw/US20.txt) |
| US21 | Top N capitals in a continent | Yes | <a href="reports/final-evidence/screenshots/US21.jpg"><img src="reports/final-evidence/screenshots/US21.jpg" width="300" alt="US21 verified report output"></a> · [Full output](reports/final-evidence/raw/US21.txt) |
| US22 | Top N capitals in a region | Yes | <a href="reports/final-evidence/screenshots/US22.jpg"><img src="reports/final-evidence/screenshots/US22.jpg" width="300" alt="US22 verified report output"></a> · [Full output](reports/final-evidence/raw/US22.txt) |
| US23 | Population inside/outside cities by continent | Yes | <a href="reports/final-evidence/screenshots/US23.jpg"><img src="reports/final-evidence/screenshots/US23.jpg" width="300" alt="US23 verified report output"></a> · [Full output](reports/final-evidence/raw/US23.txt) |
| US24 | Population inside/outside cities by region | Yes | <a href="reports/final-evidence/screenshots/US24.jpg"><img src="reports/final-evidence/screenshots/US24.jpg" width="300" alt="US24 verified report output"></a> · [Full output](reports/final-evidence/raw/US24.txt) |
| US25 | Population inside/outside cities by country | Yes | <a href="reports/final-evidence/screenshots/US25.jpg"><img src="reports/final-evidence/screenshots/US25.jpg" width="300" alt="US25 verified report output"></a> · [Full output](reports/final-evidence/raw/US25.txt) |
| US26 | World population | Yes | <a href="reports/final-evidence/screenshots/US26.jpg"><img src="reports/final-evidence/screenshots/US26.jpg" width="300" alt="US26 verified report output"></a> · [Full output](reports/final-evidence/raw/US26.txt) |
| US27 | Continent population | Yes | <a href="reports/final-evidence/screenshots/US27.jpg"><img src="reports/final-evidence/screenshots/US27.jpg" width="300" alt="US27 verified report output"></a> · [Full output](reports/final-evidence/raw/US27.txt) |
| US28 | Region population | Yes | <a href="reports/final-evidence/screenshots/US28.jpg"><img src="reports/final-evidence/screenshots/US28.jpg" width="300" alt="US28 verified report output"></a> · [Full output](reports/final-evidence/raw/US28.txt) |
| US29 | Country population | Yes | <a href="reports/final-evidence/screenshots/US29.jpg"><img src="reports/final-evidence/screenshots/US29.jpg" width="300" alt="US29 verified report output"></a> · [Full output](reports/final-evidence/raw/US29.txt) |
| US30 | District population | Yes | <a href="reports/final-evidence/screenshots/US30.jpg"><img src="reports/final-evidence/screenshots/US30.jpg" width="300" alt="US30 verified report output"></a> · [Full output](reports/final-evidence/raw/US30.txt) |
| US31 | City population | Yes | <a href="reports/final-evidence/screenshots/US31.jpg"><img src="reports/final-evidence/screenshots/US31.jpg" width="300" alt="US31 verified report output"></a> · [Full output](reports/final-evidence/raw/US31.txt) |
| US32 | Language speakers and world percentages | Yes | <a href="reports/final-evidence/screenshots/US32.jpg"><img src="reports/final-evidence/screenshots/US32.jpg" width="300" alt="US32 verified report output"></a> · [Full output](reports/final-evidence/raw/US32.txt) |

The assessed `master` release still needs to be prepared from the verified work.
These screenshots do not establish that Moodle submission, contribution records,
or the other Part 2 checklist items are complete.

## Automated build and tests

[GitHub Actions: Build and test](https://github.com/LexHaus/Population-Reporting-System/actions/workflows/build.yml)
 runs on pushes and pull requests. It starts MySQL with the supplied World data,
 runs the six Python calculation tests and Maven's unit and JDBC integration tests,
 builds the executable JAR, and builds the Docker image on Linux using `Dockerfile`.
 It then runs all 32 report commands inside Docker.

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

### Report implementation verification

The report-completion implementation adds the missing country, capital-city and
population reports and accepts user-selected locations/Top N for city reports.
Local verification: 48 Java tests passed without skips, all 32 packaged report
commands succeeded in Docker, and Java line coverage was 95.3% (201/211 lines).
See [report verification](reports/README.md) for commands and raw output.
