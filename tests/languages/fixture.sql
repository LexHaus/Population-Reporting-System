-- Synthetic data only. Load into a disposable test database, never the World DB.
CREATE TABLE country (
    Code CHAR(3) PRIMARY KEY,
    Population INT NOT NULL
);
CREATE TABLE countrylanguage (
    CountryCode CHAR(3) NOT NULL,
    Language VARCHAR(30) NOT NULL,
    IsOfficial CHAR(1) NOT NULL,
    Percentage DECIMAL(4,1) NOT NULL,
    PRIMARY KEY (CountryCode, Language)
);
INSERT INTO country VALUES ('AAA', 1000), ('BBB', 2000), ('CCC', 1000);
INSERT INTO countrylanguage VALUES
    ('AAA', 'Chinese', 'T', 50.0),
    ('AAA', 'English', 'F', 20.0),
    ('AAA', 'Hindi', 'F', 10.0),
    ('AAA', 'Spanish', 'F', 5.0),
    ('AAA', 'Arabic', 'F', 2.5),
    ('BBB', 'Chinese', 'F', 10.0),
    ('BBB', 'English', 'T', 50.0),
    ('BBB', 'Hindi', 'F', 5.0),
    ('BBB', 'Spanish', 'F', 20.0),
    ('BBB', 'Arabic', 'F', 5.0),
    ('CCC', 'French', 'T', 100.0);
