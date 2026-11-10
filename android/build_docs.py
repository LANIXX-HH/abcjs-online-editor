#!/usr/bin/env python3
"""Turn README.md into a self-contained help page (docs.html) for the offline Android app.

Usage: python3 android/build_docs.py README.md <output docs.html>
Needs: pip install markdown
"""
import re
import sys

import markdown

REPO = "https://github.com/LANIXX-HH/abcjs-online-editor"

src, out = sys.argv[1], sys.argv[2]
md = open(src, encoding="utf-8").read()
body = markdown.markdown(md, extensions=["tables", "fenced_code"])

# relative links (e.g. android/README.md) point to GitHub; all links open outside the app
body = re.sub(r'href="(?!https?:|#|mailto:)([^"]+)"', lambda m: f'href="{REPO}/blob/main/{m.group(1)}"', body)
body = body.replace("<a href=", '<a target="_blank" rel="noopener" href=')

page = f"""<!DOCTYPE html>
<html lang="de"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Anleitung – ABC-Noten-Editor</title>
<style>
:root{{--bg:#FFFFFF;--text:#1A1D21;--text2:#5A6070;--border:#C8CDD4;--code:#EAECEF;--link:#185FA5}}
.dark{{--bg:#23262C;--text:#E4E6EB;--text2:#9198A8;--border:#44474F;--code:#2C2F38;--link:#4D9EE8}}
html,body{{margin:0;background:var(--bg);color:var(--text)}}
body{{font-family:system-ui,-apple-system,"Segoe UI",sans-serif;font-size:15px;line-height:1.55;padding:14px 16px 40px;max-width:820px;margin:0 auto}}
h1{{font-size:22px;margin:4px 0 10px}} h2{{font-size:18px;margin:22px 0 6px;border-bottom:1.5px solid var(--border);padding-bottom:3px}}
h3{{font-size:15.5px;margin:16px 0 4px}}
a{{color:var(--link)}} code{{background:var(--code);border-radius:4px;padding:1px 4px;font-size:13px}}
table{{border-collapse:collapse;width:100%;margin:8px 0;font-size:14px;display:block;overflow-x:auto}}
th,td{{border:1px solid var(--border);padding:5px 7px;text-align:left;vertical-align:top}}
th{{background:var(--code)}} ul{{padding-left:20px}} li{{margin:2px 0}}
</style></head>
<body>
<script>if(/[?&]theme=dark/.test(location.search))document.documentElement.className='dark';</script>
{body}
</body></html>
"""
open(out, "w", encoding="utf-8").write(page)
print(f"docs written: {out} ({len(page)} bytes)")
