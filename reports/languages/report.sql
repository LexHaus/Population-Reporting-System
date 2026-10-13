-- Estimated speakers from the World database, not current population figures.
-- Keep fractional estimates until display; use all countries as the denominator.
SELECT
    requested.Language AS language,
    COALESCE(SUM(c.Population * cl.Percentage / 100.0), 0) AS estimated_speakers,
    COALESCE(SUM(c.Population * cl.Percentage / 100.0), 0)
        * 100.0 / NULLIF(world.total_population, 0) AS world_population_percentage
FROM (
    SELECT 'Chinese' AS Language
    UNION ALL SELECT 'English'
    UNION ALL SELECT 'Hindi'
    UNION ALL SELECT 'Spanish'
    UNION ALL SELECT 'Arabic'
) AS requested
LEFT JOIN countrylanguage AS cl ON cl.Language = requested.Language
LEFT JOIN country AS c ON c.Code = cl.CountryCode
CROSS JOIN (SELECT SUM(Population) AS total_population FROM country) AS world
GROUP BY requested.Language, world.total_population
ORDER BY estimated_speakers DESC, requested.Language ASC;
