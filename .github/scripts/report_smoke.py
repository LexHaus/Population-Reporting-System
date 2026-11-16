"""Exercise all 32 report commands in the packaged Docker application."""
import argparse
import json
from pathlib import Path
import subprocess


def arguments(number):
    result = ['report', f'US{number:02d}']
    if number in (2, 5, 8, 13, 18, 21, 27):
        result += ['--continent', 'Asia']
    if number in (3, 6, 9, 14, 19, 22, 28):
        result += ['--region', 'Eastern Asia']
    if number in (10, 15, 29):
        result += ['--country', 'JPN']
    if number in (11, 16, 30):
        result += ['--district', 'Tokyo-to', '--country', 'JPN']
    if number in (4, 5, 6, 12, 13, 14, 15, 16, 20, 21, 22):
        result += ['--top', '3']
    if number == 31:
        result += ['--city-id', '1532']
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--image', required=True)
    parser.add_argument('--network', default='host')
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    args.output.mkdir(parents=True, exist_ok=True)
    prefix = ['docker', 'run', '--rm', '--network', args.network]
    for name in ['DB_HOST', 'DB_PORT', 'DB_NAME', 'DB_USER', 'DB_PASSWORD']:
        prefix += ['-e', name]
    prefix += [args.image]
    evidence = []
    for number in range(1, 33):
        command = arguments(number)
        result = subprocess.run(prefix + command, text=True, capture_output=True, check=True)
        if number == 32:
            assert 'Chinese' in result.stdout and '19.61' in result.stdout
        else:
            assert f'US{number:02d}' in result.stdout
            assert 'Rows: ' in result.stdout and 'Rows: 0' not in result.stdout
            if '--top' in command:
                assert 'Rows: 3' in result.stdout
            if number == 26:
                assert '6078749450' in result.stdout
        path = args.output / f'US{number:02d}.txt'
        path.write_text(result.stdout)
        evidence.append({'requirement': f'US{number:02d}', 'arguments': command, 'exit_code': result.returncode})
        print(f'US{number:02d}: passed')
    (args.output / 'commands.json').write_text(json.dumps(evidence, indent=2) + '\n')
