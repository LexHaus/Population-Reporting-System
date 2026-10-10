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