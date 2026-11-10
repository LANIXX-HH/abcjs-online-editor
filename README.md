# ABC-Noten-Editor

Ein Noten-Editor für Chorstimmen und Lieder – als **einzelne HTML-Datei** und als **Android-App**. Noten gibt man über eine Klaviatur ein (oder singt sie ins Mikrofon), sieht sofort das Notenbild, kann es abspielen, mit Liedtext versehen, drucken und exportieren. ABC- oder Programmierkenntnisse sind nicht nötig.

Oberfläche in **Deutsch / English / Русский**, hell und dunkel.

## Aufrufen

1. **Online:** https://lanixx-hh.github.io/abcjs-online-editor/
2. **Lokal:** `index.html` herunterladen und im Browser öffnen.
3. **Android-App:** APK aus den [Releases](https://github.com/LANIXX-HH/abcjs-online-editor/releases) installieren (siehe [android/README.md](android/README.md)).

Web-Version und App funktionieren gleich. Die App läuft komplett offline; die Web-Version lädt Notensatz und Klänge aus dem Internet.

**Welche Version läuft?** Im Menü steht neben „Einstellungen“ der Versionsstempel (z. B. `c99a111 · 10.11.2026`). Sieht etwas nach altem Stand aus: Seite neu laden bzw. Tab schließen und neu öffnen.

## Aufbau der Oberfläche

| Bereich | Inhalt |
|---|---|
| **Oben** | ☰ Menü · 🎤 Singen → Noten · **Text** (Liedtext) · **`</>`** ABC-Code · 🌙/☀︎ Hell/Dunkel · ▶︎ Abspielen |
| **Mitte** | Das Notenbild – hier arbeitet man |
| **Rechts** | Klaviatur zum Eingeben, darüber/darunter die Werkzeuge |
| **Menü** | Einstellungen, Stimmen, Export, Vorlagen, gespeicherte Lieder, Sprache |

Auf dem Handy öffnet ☰ das Menü als Seitenleiste; am Computer ist es immer sichtbar.

## Noten eingeben

### Klaviatur
- Volles Klavier (88 Tasten), senkrecht, zum Scrollen. Jede Taste ist beschriftet: Buchstabe und Solfège (z. B. **C Do**).
- **Tonart-Markierung:** Töne der gewählten Tonart haben einen **grauen Punkt**, der Grundton einen größeren **dunkelroten Punkt**. Beim Öffnen steht der Grundton der Stimmlage unten.
- Taste antippen = Note wird eingefügt und erklingt. Auch Töne ohne Punkt sind wählbar.

### Werkzeuge
- **Länge** (gelber Knopf ♩ ▾): Ganze, Halbe, Viertel, Achtel, Sechzehntel – bezogen auf die Einheitslänge (siehe Einstellungen).
- **Zeichen ▾:** neue Zeile, Wiederholung auf `|:`, Wiederholung zu `:|`.
- **Pause**, **Takt |**, **⌫ Löschen** (löscht vor der Einfügemarke).

### Tonart (Quintenzirkel)
Der gelbe Knopf unten zeigt die Tonart, z. B. **C-Dur / Do Major**. Antippen öffnet den Quintenzirkel:
- außen die Vorzeichen als kleines Notensystem, Mitte Dur, innen Moll (jeweils Name und Solfège);
- einmal antippen = Tonart wird unten groß angezeigt (Vorzeichen, Name, Anzahl ♯/♭);
- nochmal antippen oder **Übernehmen** = Tonart setzen. Ges/Fis und es/dis sind geteilte Felder.

### Noten im Notenbild bearbeiten
- **Note antippen:** wählt sie aus, setzt die Einfügemarke dahinter und spielt sie ab. Neue Noten werden an dieser Stelle eingefügt.
- **Note ziehen** (Tonhöhe ändern): nur wenn unter *Einstellungen → Bearbeiten → Noten ziehen* eingeschaltet. Standardmäßig aus, damit beim Scrollen nichts verrutscht.

### Singen statt tippen
- **🎤** oben antippen und singen oder summen – erkannte Töne werden als Noten in die aktive Stimme geschrieben. Unten erscheint eine Anzeige (Note, Frequenz, Pegel); **Stop** beendet.
- Einstellungen dazu im Menü unter *Mikrofon*: Stimmlage, Empfindlichkeit, Rauschunterdrückung.
- Tipp: Töne klar absetzen, ruhige Umgebung.

## Mehrere Stimmen

Im Menü unter **Stimmen**:
- **Modus:** Einstimmig oder Mehrstimmig (SATB), **Anzahl** 2–4 Stimmen.
- **Bündelung der Systeme:** jede Stimme einzeln · paarweise (1+2, 3+4) · 1+2 zusammen, Rest einzeln.
- Pro Stimme **Name** und **Notenschlüssel**.

Über dem Notenbild erscheinen dann Stimmen-Tabs; der gewählte Tab bestimmt, in welche Stimme eingegeben wird. Die Klaviatur zeigt pro Stimme den passenden Tonumfang.

## Liedtext

Knopf **Text** oben öffnet die Liedtext-Ebene (Noten sind dabei gesperrt):
- Unter jedem System ein Feld pro Textzeile. Liegen zwei Stimmen auf einem System, gehört der Text zur oberen.
- **+ Text** fügt eine weitere Zeile (Strophe) hinzu, **×** entfernt sie.
- Unter jedem Feld: *Noten · Silben* – grün, wenn es passt.
- Schreibweise: Silben mit `-` trennen (`Lo-be den Herr-en`), `_` hält eine Silbe über die nächste Note, `*` überspringt eine Note, `|` springt zum nächsten Takt.
- Ganz unten **+ Freitext**: Text, der unter das ganze Stück gedruckt wird.

Erneut **Text** antippen kehrt zur Noteneingabe zurück.

## Abspielen
**▶︎** spielt das Stück; die gerade klingende Note wird markiert (bei offenem ABC-Code auch die Stelle im Code). **⏸** pausiert.

## ABC-Code
**`</>`** blendet den ABC-Code über dem Notenbild ein:
- direkt bearbeiten, mit **↻ ABC übernehmen** übernehmen; **Kopieren** kopiert ihn.
- Eigener ABC-Code (z. B. aus anderen Programmen) kann hier eingefügt werden. Alles, was der Editor nicht selbst bearbeitet – Liedtext, Bögen, Akkorde, Verzierungen, Kommentare, Halsrichtung usw. –, bleibt unverändert erhalten.
- **📂 Datei öffnen** (nur App): `.abc`-/Textdatei vom Gerät oder aus Google Drive laden. Enthält die Datei mehrere Stücke (`X:`), wählt man eines aus.
- Leerzeilen innerhalb eines Stücks werden zu `%` (in ABC beendet eine Leerzeile sonst das Stück).

## Einstellungen (Menü)

| Bereich | Inhalt |
|---|---|
| **Lied-Info** | Titel, Komponist, Taktart, Tempo, Einheitslänge – Taktart/Tempo/Einheitslänge auch „— Keine —“ |
| **Stimmen** | Ein-/Mehrstimmig, Anzahl, Bündelung, Name und Schlüssel je Stimme |
| **Bearbeiten** | Noten ziehen ein/aus |
| **Mikrofon** | Stimmlage, Empfindlichkeit, Rauschunterdrückung |
| **Darstellung** | Notenschlüssel (einstimmig), Transponieren – ändert nur die Anzeige und den Druck, nicht die Noten |
| **Export** | siehe unten |
| **Vorlagen** | C-Dur Tonleiter, Hänschen Klein, Walzer, SATB-Beispiel |
| **Gespeicherte Lieder** | eigenes Lied speichern, laden, löschen (im Browser bzw. in der App) |

**Einheitslänge:** Ein Klick schreibt z. B. `C`; wie lang dieses `C` ist, bestimmt die Einheitslänge (1/4 = Viertel, 1/2 = Halbe …). Umstellen ändert die Darstellung aller Noten entsprechend.

Sprache (DE/EN/RU) wird oben im Menü gewählt. Ganz unten im Menü: **📖 Anleitung** (diese Seite – in der App offline eingebaut) und **Projekt auf GitHub**.

## Drucken & Export

| | Web | App |
|---|---|---|
| **PDF-Vorschau / Drucken** | Druckdialog öffnet sich direkt; dort „Als PDF speichern“ wählen | Noten drucken (Android-Druck, auch als PDF) |
| **ABC-Datei** | ✓ | ✓ |
| **MIDI** | – | ✓ |
| **MP3** | – | ✓ |

Gedruckt wird auf A4, Zeilen wie im Editor, Systeme werden nicht über Seitenränder getrennt.

## Technik (kurz)

- Eine HTML-Datei, JavaScript ohne Build-Schritt; Notensatz und Klang mit [abcjs](https://abcjs.net).
- Gespeichert wird lokal (localStorage); Lieder, Sprache, Theme und Einstellungen bleiben auf dem Gerät.

## Veröffentlichung

- **Web:** Jeder Push auf `main` wird per GitHub Actions nach GitHub Pages veröffentlicht (`.github/workflows/pages.yml`, inkl. Versionsstempel).
- **App:** siehe [android/README.md](android/README.md).
