# Report commands and requirement mapping

The application implements all 32 report commands. Local MySQL verification runs
48 Java tests with no skips and executes all 32 packaged report commands in Docker.
This is functional verification, not a claim that final submission screenshots,
release preparation or Moodle submission are finished.

## Run a report

Build the JAR/image and start the database using the [application setup](../Population-Reporting-System/README.md).
From the application directory, replace the example arguments as needed:

```sh
docker compose run --rm app report US05 --continent "Europe" --top 7
docker compose run --rm app report US15 --country "United States" --top 5
docker compose run --rm app report US16 --district "California" --country USA --top 3
docker compose run --rm app report US21 --continent "Asia" --top 4
docker compose run --rm app report US23
docker compose run --rm app report US30 --district "California" --country USA
docker compose run --rm app report US31 --city-id 1532
```

The same arguments work after `java -jar target/Population-Reporting-System-1.0-SNAPSHOT.jar`.
Use `--help` to display usage. Country arguments accept a country code or full
name. Multiword names must be quoted. Top N must be a positive integer; zero,
negative, nonnumeric and overflowing inputs are rejected before connecting.
Missing, duplicate or irrelevant options are also rejected. SQL values are bound
parameters; user text is never interpolated into SQL.

District reports require a country to distinguish districts with the same name.
City population uses the unique World `city.ID`, so similarly named cities cannot
be silently merged. Tokyo is city ID 1532 in the supplied dump. Unknown locations
produce an empty table and an explicit "No matching records" message.

## Implemented reports

Examples below use the explicit `report` command. Each output link is actual
stdout from the packaged Java 17 Docker application on the supplied World data.

| ID | Report | Example arguments | Verified output |
|---|---|---|---|
| US01 | All countries worldwide | `report US01` | [Output](verification-output/US01.txt) |
| US02 | All countries in a continent | `report US02 --continent Asia` | [Output](verification-output/US02.txt) |
| US03 | All countries in a region | `report US03 --region 'Eastern Asia'` | [Output](verification-output/US03.txt) |
| US04 | Top N countries worldwide | `report US04 --top 3` | [Output](verification-output/US04.txt) |
| US05 | Top N countries in a continent | `report US05 --continent Asia --top 3` | [Output](verification-output/US05.txt) |
| US06 | Top N countries in a region | `report US06 --region 'Eastern Asia' --top 3` | [Output](verification-output/US06.txt) |
| US07 | All cities worldwide | `report US07` | [Output](verification-output/US07.txt) |
| US08 | All cities in a continent | `report US08 --continent Asia` | [Output](verification-output/US08.txt) |
| US09 | All cities in a region | `report US09 --region 'Eastern Asia'` | [Output](verification-output/US09.txt) |
| US10 | All cities in a country | `report US10 --country JPN` | [Output](verification-output/US10.txt) |
| US11 | All cities in a district | `report US11 --district Tokyo-to --country JPN` | [Output](verification-output/US11.txt) |
| US12 | Top N cities worldwide | `report US12 --top 3` | [Output](verification-output/US12.txt) |
| US13 | Top N cities in a continent | `report US13 --continent Asia --top 3` | [Output](verification-output/US13.txt) |
| US14 | Top N cities in a region | `report US14 --region 'Eastern Asia' --top 3` | [Output](verification-output/US14.txt) |
| US15 | Top N cities in a country | `report US15 --country JPN --top 3` | [Output](verification-output/US15.txt) |
| US16 | Top N cities in a district | `report US16 --district Tokyo-to --country JPN --top 3` | [Output](verification-output/US16.txt) |
| US17 | All capitals worldwide | `report US17` | [Output](verification-output/US17.txt) |
| US18 | All capitals in a continent | `report US18 --continent Asia` | [Output](verification-output/US18.txt) |
| US19 | All capitals in a region | `report US19 --region 'Eastern Asia'` | [Output](verification-output/US19.txt) |
| US20 | Top N capitals worldwide | `report US20 --top 3` | [Output](verification-output/US20.txt) |
| US21 | Top N capitals in a continent | `report US21 --continent Asia --top 3` | [Output](verification-output/US21.txt) |
| US22 | Top N capitals in a region | `report US22 --region 'Eastern Asia' --top 3` | [Output](verification-output/US22.txt) |
| US23 | Population inside/outside cities by continent | `report US23` | [Output](verification-output/US23.txt) |
| US24 | Population inside/outside cities by region | `report US24` | [Output](verification-output/US24.txt) |
| US25 | Population inside/outside cities by country | `report US25` | [Output](verification-output/US25.txt) |
| US26 | World population | `report US26` | [Output](verification-output/US26.txt) |
| US27 | Continent population | `report US27 --continent Asia` | [Output](verification-output/US27.txt) |
| US28 | Region population | `report US28 --region 'Eastern Asia'` | [Output](verification-output/US28.txt) |
| US29 | Country population | `report US29 --country JPN` | [Output](verification-output/US29.txt) |
| US30 | District population | `report US30 --district Tokyo-to --country JPN` | [Output](verification-output/US30.txt) |
| US31 | City population | `report US31 --city-id 1532` | [Output](verification-output/US31.txt) |
| US32 | Language speakers and world percentages | `report US32` | [Output](verification-output/US32.txt) |

## Report semantics

Country reports include code, name, continent, region, population and capital
name. Countries without a capital remain in country reports with `N/A`.
Capital reports include only actual capital cities and show name, country and
population. City reports show name, country, district and population. Ranked
reports sort descending by population, then name and unique identifier to make
ties deterministic.

Population breakdowns aggregate cities per country before adding country totals;
this avoids counting a country's population once per city. Countries without
cities contribute zero urban population and their full population to the outside
cities total. Percentages are rounded half up to two decimals; a zero denominator
shows `N/A`. District population is the sum of cities recorded in that district
(the dataset has no separate district population table). World totals use country
populations, not the sum of the city records. These are historical sample data.

## Compatibility

`languages`, `countries`, `cities [US07..US16]` and no-argument startup remain
available as demo shortcuts. Country demo output now includes all required
columns. The short city demos retain their former Europe/Western Europe/USA/
California filters and Top 10. Use `report USxx` with explicit options for your
own inputs. `capitals` defaults to worldwide Top 10 and `population` to World.
The original city SQL file remains a reference for those older demo queries.

## Verification and remaining assessment tasks

[Recorded test log](verification-output/maven-verification.txt),
[verification totals](verification-output/verification.json), and
[exact report arguments](verification-output/commands.json) accompany the output.
Local tests use a disposable MySQL instance. Synthetic edge cases use a separate
schema, dropped after testing; the original World tables are preserved.

GitHub Actions now runs all 32 reports in Docker and uploads their outputs along
with tests and coverage. Run this locally with the database connection environment
set and the built image available:

```sh
python3 .github/scripts/report_smoke.py --image population-reporting-system:ci --output /tmp/report-evidence
```

The script defaults to host networking for Linux CI. On Docker Desktop use
`--network bridge`, `DB_HOST=host.docker.internal`, and the published database port.
After merging, capture final screenshots for the newly completed requirements,
review the completion table as a team and prepare the assessed `master` release.
