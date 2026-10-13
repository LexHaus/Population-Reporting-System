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

The container setup targets MySQL 8.4. Java packaging and local JDBC execution
were checked during review; a full Docker run remains to be verified on a
machine with Docker installed.
