# Population-Reporting-System
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
