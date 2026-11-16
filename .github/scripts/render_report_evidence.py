"""Build clearly labelled screenshot viewers from saved Docker report stdout."""
import argparse
import html
import json
from pathlib import Path
import re
import shlex

STYLE = '''body{margin:0;padding:28px;background:#eef2f6;color:#182434;font:16px system-ui}main{max-width:1420px;margin:auto;background:white;padding:28px;border:1px solid #ced7e1;border-radius:10px}h1{font-size:27px;margin:0 0 12px}p{line-height:1.5}code,pre{font:14px/1.5 ui-monospace,monospace}code{background:#edf2f7;padding:8px;display:block;white-space:pre-wrap}table{border-collapse:collapse;width:100%;font-size:15px}th,td{padding:9px 10px;text-align:left;border-bottom:1px solid #d8e1ea}th{background:#192c40;color:white}tr:nth-child(even){background:#f3f6fa}small{color:#526477}a{color:#1552a1}pre{white-space:pre-wrap;background:#192c40;color:#e8f0ff;padding:20px}.note{background:#fff5d5;padding:10px}'''


def render(root, mapping, commit, date):
    viewers = root / 'viewers'
    viewers.mkdir(parents=True, exist_ok=True)
    commands = json.loads((root / 'raw/commands.json').read_text())
    names = {}
    for line in mapping.read_text().splitlines():
        if re.match(r'\| US\d\d \|', line):
            parts = [part.strip() for part in line.split('|')]
            names[parts[1]] = parts[2]
    for record in commands:
        report_id = record['requirement']
        lines = (root / 'raw' / (report_id + '.txt')).read_text().splitlines()
        if report_id == 'US32':
            body = '<pre>' + html.escape('\n'.join(lines)) + '</pre>'
            count = 5
            shown = 5
        else:
            headings = lines[1].split('\t')
            count_line = next(i for i in range(len(lines)-1, 1, -1) if lines[i].startswith('Rows: '))
            rows = [line.split('\t') for line in lines[2:count_line]]
            count = int(lines[count_line].split(': ')[1])
            assert count == len(rows)
            shown = min(12, count)
            body = '<table><thead><tr>' + ''.join('<th>'+html.escape(h)+'</th>' for h in headings) + '</tr></thead><tbody>'
            body += ''.join('<tr>'+''.join('<td>'+html.escape(cell)+'</td>' for cell in row)+'</tr>' for row in rows[:shown])
            body += '</tbody></table>'
        command = 'docker run [database environment] population-reporting-system:final-evidence ' + shlex.join(record['arguments'])
        document = '<!doctype html><meta charset="utf-8"><title>'+report_id+' — '+html.escape(names[report_id])+'</title><style>'+STYLE+'</style><main>'
        document += '<h1>'+report_id+' — '+html.escape(names[report_id])+'</h1><p>'+html.escape(date)+' · Java 17 Docker application · MySQL 8.4.11 · Exit status: '+str(record['exit_code'])+'</p>'
        document += '<code>'+html.escape(command)+'</code><p>Displayed '+str(shown)+' of '+str(count)+' result rows. <a href="../raw/'+report_id+'.txt">Complete unchanged stdout</a>.</p>'+body
        document += '<p class="note">Evidence viewer of actual Docker report output. Tab-separated columns are formatted as a table; long results show the first 12 rows. This is not an application web interface.</p>'
        document += '<p><small>Source commit: '+html.escape(commit)+'<br>Historical World sample dataset, not current population statistics. Aggregate percentages are rounded to two decimal places.</small></p></main>'
        (viewers / (report_id+'.html')).write_text(document)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--root', type=Path, required=True)
    parser.add_argument('--mapping', type=Path, required=True)
    parser.add_argument('--commit', required=True)
    parser.add_argument('--date', required=True)
    args = parser.parse_args()
    render(args.root, args.mapping, args.commit, args.date)
