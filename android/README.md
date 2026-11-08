# Android-App (WebView-Wrapper)

Verpackt den Web-Editor (`index.html` aus dem Repo-Wurzelverzeichnis) als native Android-App. Die App läuft **offline**: der Build bettet `index.html` und `abcjs` in die App-Assets ein.

## Was drin ist
- **WebView** lädt `file:///android_asset/index.html`.
- **Mikrofon** (Singen → Noten): `RECORD_AUDIO` wird zur Laufzeit abgefragt und an die WebView durchgereicht.
- **Eigene Lieder**: `localStorage` (DOM storage) aktiv.
- **Export** (MIDI/WAV/ABC): `data:`- und `blob:`-Downloads werden abgefangen und unter *Downloads* gespeichert.
- **Zurück-Taste**: navigiert in der WebView zurück.

## Single source of truth
Die App hat **keine eigene Kopie** von `index.html`. Der Build kopiert die Datei aus dem Repo-Wurzelverzeichnis und biegt die abcjs-CDN-URL auf die lokal eingebettete Datei um. Änderungen am Editor also immer an der Wurzel-`index.html` machen.

## APK bauen

**Automatisch (empfohlen):** Jeder Push auf `main`, der `index.html` oder `android/**` ändert, baut per GitHub Actions ein Debug-APK. Es liegt danach unter *Actions → Build Android APK → Artifacts → `abc-editor-debug-apk`*.

**Lokal:**
```
# einmalig: Web-Assets einbetten
cp index.html android/app/src/main/assets/index.html
curl -fsSL -o android/app/src/main/assets/abcjs-basic-min.js \
  https://cdn.jsdelivr.net/npm/abcjs@6.4.3/dist/abcjs-basic-min.js
sed -i 's#https://cdn.jsdelivr.net/npm/abcjs@6.4.3/dist/abcjs-basic-min.js#abcjs-basic-min.js#' \
  android/app/src/main/assets/index.html

cd android && gradle assembleDebug
# Ergebnis: app/build/outputs/apk/debug/app-debug.apk
```

Das Debug-APK ist mit dem Debug-Schlüssel signiert und direkt installierbar (Installation aus unbekannten Quellen erlauben). Für einen Play-Store-Upload wäre zusätzlich ein Release-Signing mit eigenem Keystore nötig.
