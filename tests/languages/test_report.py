"""Run the actual report SQL against synthetic data, without Maven dependencies.

SQLite verifies calculation logic; it does not replace a MySQL integration test.
"""
import pathlib
import sqlite3
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[2]
QUERY = (ROOT / 'reports/languages/report.sql').read_text()
FIXTURE = (ROOT / 'tests/languages/fixture.sql').read_text()


class LanguageReportTest(unittest.TestCase):
    def setUp(self):
        self.db = sqlite3.connect(':memory:')
        self.addCleanup(self.db.close)
        self.db.executescript(FIXTURE)

    def report(self):
        return self.db.execute(QUERY).fetchall()

    def test_totals_filter_order_and_world_denominator(self):
        expected = [('English', 1200, 30), ('Chinese', 700, 17.5),
                    ('Spanish', 450, 11.25), ('Hindi', 200, 5),
                    ('Arabic', 125, 3.125)]
        self.assertEqual(self.report(), expected)

    def test_missing_language_still_has_zero_row(self):
        self.db.execute("DELETE FROM countrylanguage WHERE Language = 'Arabic'")
        self.assertEqual(self.report()[-1], ('Arabic', 0, 0))

    def test_empty_world_has_undefined_percentage(self):
        self.db.execute('DELETE FROM countrylanguage')
        self.db.execute('DELETE FROM country')
        rows = self.report()
        self.assertEqual(len(rows), 5)
        self.assertTrue(all(count == 0 and percent is None for _, count, percent in rows))

    def test_zero_world_population(self):
        self.db.execute('UPDATE country SET Population = 0')
        self.assertTrue(all(count == 0 and percent is None
                            for _, count, percent in self.report()))

    def test_fractional_estimates_not_rounded_per_country(self):
        self.db.execute("UPDATE country SET Population = 1001 WHERE Code = 'AAA'")
        english = self.report()[0]
        self.assertAlmostEqual(english[1], 1200.2)
        self.assertAlmostEqual(english[2], 1200.2 / 4001 * 100)

    def test_ties_use_alphabetical_order(self):
        self.db.execute('UPDATE countrylanguage SET Percentage = 0')
        self.assertEqual([row[0] for row in self.report()],
                         ['Arabic', 'Chinese', 'English', 'Hindi', 'Spanish'])


if __name__ == '__main__':
    unittest.main()
