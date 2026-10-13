# Running the shared application

Requires Java 17 or newer, Maven, and Docker Compose.

From this directory:

1. Copy `.env.example` to `.env` and choose a local database password.
2. Run `mvn package` to build the self-contained JAR.
3. Run `docker compose up --build` to initialise MySQL and run the app.

Compose waits for the World country table to become queryable before starting
Java. Database connection failures return a nonzero exit status.

The database import runs only when the Docker data volume is first created.
Changing the SQL dump or password does not reinitialise an existing volume.
Keep local passwords in the ignored `.env` file.

The container setup targets MySQL 8.4. The language application container and
JDBC integration test passed against MySQL 8.4.11 on 13 October 2026. The test
used host port 33306 because 3306 was occupied; internal networking was unchanged.
Run `docker compose run --rm app languages` to display the language report.
