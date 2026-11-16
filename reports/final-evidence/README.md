# Final assessment report evidence

Captured 15 November 2026 from merged `develop` commit
`105ac1c30a918e1852a1044f46e3f7b31bd36a19` (PR #39).

All 32 commands were executed inside `population-reporting-system:final-evidence`
against a fresh MySQL 8.4.11 instance with the supplied World SQL. Container
connection: `host.docker.internal:33310/world` through the host-published port.
No existing user database was changed. This proves local Docker execution, not a
hosted service.

## Screenshots and original output

Each screenshot captures a real browser rendering of a labelled evidence viewer.
For tabular reports, the viewer formats the captured tab-separated values and
shows up to 12 rows. The full output and total result count are retained. US32
shows the captured language stdout directly. The viewer source is saved in
`viewers/`; regenerate it using `.github/scripts/render_report_evidence.py`.
This does not represent a web interface of the application or IntelliJ output.

| Requirement | Name | Screenshot | Complete stdout | Viewer |
|---|---|---|---|---|
| US01 | All countries worldwide | [Image](screenshots/US01.jpg) | [Output](raw/US01.txt) | [HTML](viewers/US01.html) |
| US02 | All countries in a continent | [Image](screenshots/US02.jpg) | [Output](raw/US02.txt) | [HTML](viewers/US02.html) |
| US03 | All countries in a region | [Image](screenshots/US03.jpg) | [Output](raw/US03.txt) | [HTML](viewers/US03.html) |
| US04 | Top N countries worldwide | [Image](screenshots/US04.jpg) | [Output](raw/US04.txt) | [HTML](viewers/US04.html) |
| US05 | Top N countries in a continent | [Image](screenshots/US05.jpg) | [Output](raw/US05.txt) | [HTML](viewers/US05.html) |
| US06 | Top N countries in a region | [Image](screenshots/US06.jpg) | [Output](raw/US06.txt) | [HTML](viewers/US06.html) |
| US07 | All cities worldwide | [Image](screenshots/US07.jpg) | [Output](raw/US07.txt) | [HTML](viewers/US07.html) |
| US08 | All cities in a continent | [Image](screenshots/US08.jpg) | [Output](raw/US08.txt) | [HTML](viewers/US08.html) |
| US09 | All cities in a region | [Image](screenshots/US09.jpg) | [Output](raw/US09.txt) | [HTML](viewers/US09.html) |
| US10 | All cities in a country | [Image](screenshots/US10.jpg) | [Output](raw/US10.txt) | [HTML](viewers/US10.html) |
| US11 | All cities in a district | [Image](screenshots/US11.jpg) | [Output](raw/US11.txt) | [HTML](viewers/US11.html) |
| US12 | Top N cities worldwide | [Image](screenshots/US12.jpg) | [Output](raw/US12.txt) | [HTML](viewers/US12.html) |
| US13 | Top N cities in a continent | [Image](screenshots/US13.jpg) | [Output](raw/US13.txt) | [HTML](viewers/US13.html) |
| US14 | Top N cities in a region | [Image](screenshots/US14.jpg) | [Output](raw/US14.txt) | [HTML](viewers/US14.html) |
| US15 | Top N cities in a country | [Image](screenshots/US15.jpg) | [Output](raw/US15.txt) | [HTML](viewers/US15.html) |
| US16 | Top N cities in a district | [Image](screenshots/US16.jpg) | [Output](raw/US16.txt) | [HTML](viewers/US16.html) |
| US17 | All capitals worldwide | [Image](screenshots/US17.jpg) | [Output](raw/US17.txt) | [HTML](viewers/US17.html) |
| US18 | All capitals in a continent | [Image](screenshots/US18.jpg) | [Output](raw/US18.txt) | [HTML](viewers/US18.html) |
| US19 | All capitals in a region | [Image](screenshots/US19.jpg) | [Output](raw/US19.txt) | [HTML](viewers/US19.html) |
| US20 | Top N capitals worldwide | [Image](screenshots/US20.jpg) | [Output](raw/US20.txt) | [HTML](viewers/US20.html) |
| US21 | Top N capitals in a continent | [Image](screenshots/US21.jpg) | [Output](raw/US21.txt) | [HTML](viewers/US21.html) |
| US22 | Top N capitals in a region | [Image](screenshots/US22.jpg) | [Output](raw/US22.txt) | [HTML](viewers/US22.html) |
| US23 | Population inside/outside cities by continent | [Image](screenshots/US23.jpg) | [Output](raw/US23.txt) | [HTML](viewers/US23.html) |
| US24 | Population inside/outside cities by region | [Image](screenshots/US24.jpg) | [Output](raw/US24.txt) | [HTML](viewers/US24.html) |
| US25 | Population inside/outside cities by country | [Image](screenshots/US25.jpg) | [Output](raw/US25.txt) | [HTML](viewers/US25.html) |
| US26 | World population | [Image](screenshots/US26.jpg) | [Output](raw/US26.txt) | [HTML](viewers/US26.html) |
| US27 | Continent population | [Image](screenshots/US27.jpg) | [Output](raw/US27.txt) | [HTML](viewers/US27.html) |
| US28 | Region population | [Image](screenshots/US28.jpg) | [Output](raw/US28.txt) | [HTML](viewers/US28.html) |
| US29 | Country population | [Image](screenshots/US29.jpg) | [Output](raw/US29.txt) | [HTML](viewers/US29.html) |
| US30 | District population | [Image](screenshots/US30.jpg) | [Output](raw/US30.txt) | [HTML](viewers/US30.html) |
| US31 | City population | [Image](screenshots/US31.jpg) | [Output](raw/US31.txt) | [HTML](viewers/US31.html) |
| US32 | Language speakers and world percentages | [Image](screenshots/US32.jpg) | [Output](raw/US32.txt) | [HTML](viewers/US32.html) |

## Verification

- [Exact arguments and exit statuses](raw/commands.json): 32 successful commands.
- [Maven log](maven-verification.txt): 48 tests, 0 failures/errors/skips.
- [Python log](python-verification.txt): six calculation tests passed.
- [Coverage XML](jacoco.xml) and [CSV](jacoco.csv): overall Java line coverage
  201/211 (95.3%); LanguageReport lines 20/20 and branches 7/8.
- [Metadata](metadata.json) identifies the source commit, images and environment.
- [SHA256 checksums](SHA256SUMS) cover the captured evidence files.
- [Post-merge Actions success](https://github.com/LexHaus/Population-Reporting-System/actions/runs/43786628256).

## Reproduce

Build with Maven, start a disposable World database, and build the Docker image
using the application Dockerfile. Set DB_HOST, DB_PORT, DB_NAME, DB_USER and
DB_PASSWORD for that test database. Then run:

```sh
python3 .github/scripts/report_smoke.py   --image population-reporting-system:final-evidence --network bridge   --output reports/final-evidence/raw
python3 .github/scripts/render_report_evidence.py   --root reports/final-evidence --mapping reports/README.md   --commit "$(git rev-parse HEAD)" --date '15 November 2026'
```

On Docker Desktop, DB_HOST should be `host.docker.internal` and DB_PORT the
published test port. Linux CI uses `--network host`. Serve the repository locally
and capture each `viewers/USxx.html` in a browser; the HTML generation itself does
not generate screenshots.
