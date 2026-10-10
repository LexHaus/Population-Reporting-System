-- US07 - View all cities worldwide by population
-- Displays city name, country, district and population
-- ordered from largest population to smallest.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
ORDER BY city.Population DESC;

-- US08 - View all cities in a continent by population
-- Displays city name, country, district and population
-- for cities in a selected continent, ordered largest to smallest.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
WHERE country.Continent = 'Europe'
ORDER BY city.Population DESC;

-- US09 - View all cities in a region by population
-- Displays city name, country, district and population
-- for cities in a selected region, ordered largest to smallest.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
WHERE country.Region = 'Western Europe'
ORDER BY city.Population DESC;

-- US10 - View all cities in a country by population
-- Displays city name, country, district and population
-- for cities in a selected country, ordered largest to smallest.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
WHERE country.Name = 'United States'
ORDER BY city.Population DESC;

-- US11 - View all cities in a district by population
-- Displays city name, country, district and population
-- for cities in a selected district, ordered largest to smallest.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
WHERE city.District = 'California'
ORDER BY city.Population DESC;

-- US12 - View Top N cities worldwide by population
-- Displays city name, country, district and population
-- for the Top N populated cities worldwide.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
ORDER BY city.Population DESC
    LIMIT 10;

-- US13 - View Top N cities in a continent by population
-- Displays city name, country, district and population
-- for the Top N populated cities in a selected continent.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
WHERE country.Continent = 'Europe'
ORDER BY city.Population DESC
    LIMIT 10;

-- US14 - View Top N cities in a region by population
-- Displays city name, country, district and population
-- for the Top N populated cities in a selected region.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
WHERE country.Region = 'Western Europe'
ORDER BY city.Population DESC
    LIMIT 10;

-- US15 - View Top N cities in a country by population
-- Displays city name, country, district and population
-- for the Top N populated cities in a selected country.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
WHERE country.Name = 'United States'
ORDER BY city.Population DESC
    LIMIT 10;

-- US16 - View Top N cities in a district by population
-- Displays city name, country, district and population
-- for the Top N populated cities in a selected district.

SELECT
    city.Name AS name,
    country.Name AS country,
    city.District AS district,
    city.Population AS population
FROM city
         JOIN country
              ON city.CountryCode = country.Code
WHERE city.District = 'California'
ORDER BY city.Population DESC
    LIMIT 10;