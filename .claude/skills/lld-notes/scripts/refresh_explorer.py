"""Refresh the "Run and Test" code explorer data inside an LLD notes page.

Replaces the JSON inside <script type="application/json" id="repoFiles"> with
the current source files of one problem package and a recorded console run.

Usage:
  python refresh_explorer.py PAGE PACKAGE_DIR [--output RUN.txt]
                             [--answers "100,6,5,2,1,Alice,Bob"] [--main Main.java]

--answers puts the typed input back next to each prompt. It only works when the
program prints prompts like "Enter X : " with print (not println).
"""
import argparse, json, os, re

ap = argparse.ArgumentParser()
ap.add_argument('page')
ap.add_argument('package_dir')
ap.add_argument('--output', help='file with the captured stdout of one run')
ap.add_argument('--answers', help='comma-separated stdin values, in order')
ap.add_argument('--main', default='Main.java', help='entry file, path relative to the package')
a = ap.parse_args()

files = []
for root, _, names in os.walk(a.package_dir):
    for n in sorted(names):
        if not n.endswith(('.java', '.py', '.ts', '.js', '.go', '.kt', '.cpp', '.h')):
            continue
        full = os.path.join(root, n)
        rel = os.path.relpath(full, a.package_dir).replace('\\', '/')
        files.append({'path': rel, 'code': open(full, encoding='utf-8').read().replace('\r\n', '\n')})
files.sort(key=lambda f: (f['path'].count('/') == 0, f['path']))
if not any(f['path'] == a.main for f in files):
    raise SystemExit(f'--main {a.main} not found in {a.package_dir}')

output = ''
if a.output:
    raw = open(a.output, encoding='utf-8').read().replace('\r\n', '\n')
    if a.answers:
        first, rest = raw.split('\n', 1) if '\n' in raw else (raw, '')
        prompts = [x.strip() + ' ' for x in first.split(' : ') if x.strip()]
        answers = [x.strip() for x in a.answers.split(',')]
        raw = '\n'.join(f'{p}: {v}' for p, v in zip(prompts, answers)) + '\n' + rest
    output = raw

data = json.dumps({'files': files, 'output': output, 'main': a.main}, ensure_ascii=False).replace('</', '<\\/')
s = open(a.page, encoding='utf-8').read()
pat = re.compile(r'(<script type="application/json" id="repoFiles">)(.*?)(</script>)', re.S)
if not pat.search(s):
    raise SystemExit('repoFiles block not found in page')
s = pat.sub(lambda m: m.group(1) + data + m.group(3), s, count=1)
open(a.page, 'w', encoding='utf-8').write(s)
print(f'ok: {len(files)} files, {len(output)} chars of output, main={a.main}')
