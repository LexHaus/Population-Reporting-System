"""Generate a badge from JaCoCo's aggregate Java line counter."""
import argparse
import json
from pathlib import Path
import xml.etree.ElementTree as ET


def badge_data(report):
    counter = ET.parse(report).getroot().find("counter[@type='LINE']")
    if counter is None:
        raise ValueError('JaCoCo report has no aggregate line counter')
    covered, missed = int(counter.get('covered')), int(counter.get('missed'))
    if min(covered, missed) < 0 or covered + missed == 0:
        raise ValueError('JaCoCo report has no valid measured lines')
    percentage = 100 * covered / (covered + missed)
    return {
        'schemaVersion': 1,
        'label': 'coverage: master',
        'message': f'{percentage:.1f}%',
        'color': 'brightgreen' if percentage >= 80 else 'yellow' if percentage >= 60 else 'red',
    }


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('report', type=Path)
    parser.add_argument('output', type=Path)
    parser.add_argument('--commit', required=True)
    args = parser.parse_args()
    data = badge_data(args.report)
    args.output.mkdir(parents=True, exist_ok=True)
    (args.output / 'master.json').write_text(json.dumps(data, indent=2) + '\n')
    (args.output / 'master-source.json').write_text(json.dumps({
        'commit': args.commit,
        'metric': 'Java line coverage, all application classes',
    }, indent=2) + '\n')
    (args.output / 'jacoco.xml').write_bytes(args.report.read_bytes())
