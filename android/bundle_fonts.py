#!/usr/bin/env python3
"""Bundle the Google text fonts of index.html into the app, so they also work offline.

Usage: python3 android/bundle_fonts.py <assets dir>
Reads <assets>/index.html, downloads the stylesheet of <link id="gfonts"> and every font file it
references into <assets>/fonts/, and points the link at the local copy (fonts/fonts.css).
"""
import os
import re
import sys
import urllib.request

assets = sys.argv[1]
page_path = os.path.join(assets, "index.html")
page = open(page_path, encoding="utf-8").read()

link = re.search(r'<link id="gfonts" rel="stylesheet" href="([^"]+)">', page)
css_url = link.group(1).replace("&amp;", "&")

# A current browser user agent → Google serves woff2 with all language subsets (incl. Cyrillic)
UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0 Safari/537.36"


def get(url):
    req = urllib.request.Request(url, headers={"User-Agent": UA})
    with urllib.request.urlopen(req, timeout=60) as r:
        return r.read()


css = get(css_url).decode("utf-8")
out_dir = os.path.join(assets, "fonts")
os.makedirs(out_dir, exist_ok=True)

files = {}
for url in sorted(set(re.findall(r"url\((https://[^)]+)\)", css))):
    name = "f%03d%s" % (len(files), os.path.splitext(url)[1] or ".woff2")
    open(os.path.join(out_dir, name), "wb").write(get(url))
    files[url] = name

for url, name in files.items():
    css = css.replace(url, name)
open(os.path.join(out_dir, "fonts.css"), "w", encoding="utf-8").write(css)

page = page.replace(link.group(0), '<link id="gfonts" rel="stylesheet" href="fonts/fonts.css">')
open(page_path, "w", encoding="utf-8").write(page)
size = sum(os.path.getsize(os.path.join(out_dir, n)) for n in files.values())
print("fonts bundled: %d files, %.1f MB" % (len(files), size / 1048576))
