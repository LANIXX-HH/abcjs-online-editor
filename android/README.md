# Android-App

Der ABC-Noten-Editor als Android-App. Gleiche Oberfläche und Funktionen wie die Web-Version, läuft aber **vollständig offline**: Editor, Notensatz (abcjs), MP3-Encoder und Klavierklänge sind in die App eingebaut.

## Installieren

1. In GitHub unter **[Releases](https://github.com/LANIXX-HH/abcjs-online-editor/releases)** die neueste `ABC-Editor-v….apk` (unter *Assets*) herunterladen.
2. Auf dem Handy öffnen und installieren („Installation aus unbekannten Quellen“ einmalig erlauben).
3. Beim ersten Antippen von 🎤 fragt Android nach dem Mikrofon – erlauben.

Die installierte Version steht im Menü neben „Einstellungen“.

## Was die App zusätzlich kann

- **ABC-Datei öffnen:** im ABC-Code (`</>`) → 📂 Datei öffnen. Der Android-Dateiauswahldialog zeigt das Gerät und – wenn die Drive-App installiert ist – Google Drive.
- **Noten drucken** über den Android-Druckdienst (auch „Als PDF speichern“).
- **MIDI-** und **MP3-Export** – Dateien landen unter *Downloads*.
- **Anleitung offline:** Menü → 📖 Anleitung zeigt die Bedienungsanleitung ohne Internet (wird beim Build aus `README.md` erzeugt).
- **Links** (z. B. „Projekt auf GitHub“) öffnen im Browser des Handys; das gerade bearbeitete Lied bleibt offen.
- **Zurück-Taste** schickt die App in den Hintergrund, statt sie zu schließen.

## Neue Version bauen

**Release (installierbare Version):**
GitHub → *Actions* → **Build Android APK** → *Run workflow* → Versionsnummer eintragen (z. B. `v1.2.0`) → *Run*.
Nach ein paar Minuten liegt die APK unter *Releases*.

**Nur testen:** Jeder Push auf `main`, der `index.html` oder `android/**` ändert, baut automatisch eine Test-APK. Sie liegt unter *Actions → Build Android APK → Artifacts → `abc-editor-debug-apk`*.

## Wichtig für Änderungen

Die App hat **keine eigene Kopie** des Editors. Der Build nimmt immer die `index.html` aus dem Hauptverzeichnis und baut sie offline ein. Änderungen am Editor also nur dort machen – Web und App bleiben so automatisch gleich.

## Technik (kurz)

- WebView lädt `file:///android_asset/index.html`.
- Der Build lädt `abcjs`, `lamejs` (MP3) und den Klavier-Soundfont herunter, legt sie in die App-Assets und biegt die Web-Adressen auf die lokalen Dateien um.
- Berechtigungen: `RECORD_AUDIO` und `MODIFY_AUDIO_SETTINGS` (Mikrofon), Speichern über `localStorage`.
- Die APK ist mit dem Debug-Schlüssel signiert; für den Play Store wäre ein eigener Release-Schlüssel nötig.
