
# UC01 - Generate City Population Reports

## Goal in Context
As a system user, I want to generate city population reports so that I can analyse population distribution across different geographical areas.

## Scope
Population Reporting System

## Level
User Goal

## Preconditions
- The Population Reporting System is running.
- The World database is available.
- City population data is accessible.

## Success Condition
The requested city report is generated successfully, with cities sorted from largest to smallest population.

## Failed Condition
The system is unable to generate the requested report.

## Primary Actor
System User

## Trigger
The user requests a City Population Report.

## Main Success Scenario
1. The user requests a city population report.
2. The system identifies the requested report type.
3. The user provides any required geographical filter or Top N value.
4. The system connects to the World database.
5. The system retrieves matching city records.
6. The system sorts cities by population in descending order.
7. The system generates and returns the report.

## Extensions
- Database unavailable: The system reports a database connection error.
- Invalid geographical filter: The system handles invalid input.
- No matching cities: The system returns an empty result or an appropriate message.
- Invalid Top N value: The system handles the invalid value.

## Sub-variations
- US07: All cities worldwide, largest to smallest.
- US08: All cities in a continent, largest to smallest.
- US09: All cities in a region, largest to smallest.
- US10: All cities in a country, largest to smallest.
- US11: All cities in a district, largest to smallest.
- US12: Top N cities worldwide.
- US13: Top N cities in a continent.
- US14: Top N cities in a region.
- US15: Top N cities in a country.
- US16: Top N cities in a district.

## Schedule
Delivery according to the Group 26 DevOps sprint plan.
