# ABC-Notation Noten-Editor (eigenständige HTML-Anwendung)

## Prompt

Erstelle einen vollständigen, **eigenständigen** Noten-Editor als **eine einzige HTML-Datei** (HTML + CSS + JavaScript inline, keine externen Abhängigkeiten außer der abcjs-Bibliothek per CDN). Die Anwendung soll auch für Menschen ohne Programmier- oder ABC-Kenntnisse bedienbar sein, im Stil von `editor.drawthedots.com` bzw. `abcjs.net`. Sprache der Oberfläche: **Deutsch**.

Die Datei muss heruntergeladen und lokal im Browser geöffnet voll funktionsfähig sein (offline, abgesehen vom abcjs-CDN-Skript). Sie darf **nicht** auf CSS-Variablen oder Stile einer Host-Umgebung angewiesen sein — alle Farben und Stile bringt sie selbst mit.

## Technische Grundlage

- **abcjs v6.4.3** über CDN einbinden:
  `https://cdn.jsdelivr.net/npm/abcjs@6.4.3/dist/abcjs-basic-min.js`
- Rendering der Partitur über `ABCJS.renderAbc(...)` mit `add_classes:true`, `responsive:'resize'`.
- Audio über `ABCJS.synth.SynthController` (eingebaute Abspielleiste) und `ABCJS.synth.playEvent(...)` für Einzeltöne.
- Reines Vanilla-JavaScript, kein Framework, kein Build-Schritt.
- Keine Browser-Storage-APIs außer `localStorage` (nur für die Lied-Speicherung; mit try/catch absichern, da in manchen Sandboxes blockiert).

## Layout

Ein CSS-Grid (`.wrap`) mit folgender Struktur, Höhe `100vh`:

```
┌─────────────────────────────────────────────┐
│ Stimmen-Leiste (voice-bar)                    │  ← oben, volle Breite
├─────────────────────────────────────────────┤
│ Toolbar (eingefärbt nach aktiver Stimme)      │  ← volle Breite
├──────────────┬──────────────────────────────┤
│ Sidebar      │ Hauptbereich (main):          │
│ (links,      │  - Notenvorschau (score-panel)│
│  scrollbar)  │  - Abspielleiste (player)     │
│              │  - ABC-Textfeld (abc-panel)   │
│              │  - Aktionsleiste (bottom-bar) │
└──────────────┴──────────────────────────────┘
```

Sidebar-Breite ca. 225–240px. Theme-Umschalter oben rechts in der Toolbar.

## Funktionsumfang im Detail

### 1. Noten-Eingabe-Toolbar (oben)

- **Notentasten C–B**: fügen die jeweilige Note an der Cursorposition im ABC-Feld ein. Die Beschriftung richtet sich nach dem gewählten Notennamen-System und Register (siehe unten).
- **Vorzeichen**: ♮ (kein), ♯ (`^`), ♭ (`_`), Doppelkreuz (`^^`), Doppel-B (`__`). Werden der nächsten eingefügten Note vorangestellt. Aktiver Zustand wird markiert.
- **Länge**: Buttons mit **Textbeschriftung** `1/1`, `1/2`, `1/4`, `1/8`, `1/16` (NICHT die Unicode-Musikglyphen 𝅝 𝅗𝅥 𝅘𝅥𝅯 verwenden — die rendern auf vielen Systemen als leere Kästchen!). Intern abgebildet auf ABC-Längen (`4`, `2`, `1`, `/2`, `/4`).
- **Einfügen**: Pause (`z`, Button mit Text „Pause", kein Glyph), Taktstrich (`|`), Wiederholung auf (`|:`), Wiederholung zu (`:|`), neue Zeile (`↵`), letztes Zeichen löschen (`⌫`).
- **Oktave-Zähler** (`−` / Wert / `+` / `↺`): verschiebt die **nächste eingefügte** Note oktavweise. +1 macht aus `C` ein `c`, +2 ein `c'`, −1 ein `C,` usw. Der Zähler wird blau hervorgehoben, wenn ungleich 0.
- **Transponieren-Zähler** (`−` / Wert / `+` / `↺`): rein **visuelle** Verschiebung der Anzeige in Halbtönen über `visualTranspose` von abcjs — verändert den ABC-Text NICHT. Ebenfalls blau bei ungleich 0.

### 2. Sidebar — Lied-Info

Eingabefelder, die den ABC-Header erzeugen:

- **Titel** (`T:`), **Komponist** (`C:`) — Textfelder.
- **Taktart** (`M:`) — Dropdown mit Option **„— Keine —"** (leer) sowie 4/4, 3/4, 2/4, 6/8, C. Wenn „Keine", wird keine `M:`-Zeile erzeugt.
- **Tonart** (`K:`) — Dropdown mit **„— Keine —"** und Dur-/Moll-Tonarten.
- **Tempo** (`Q:`) — Dropdown mit **„— Keine —"** und klassischen Tempobezeichnungen (40 Grave bis 200 Prestissimo).
- **Einheitslänge** (`L:`) — Dropdown mit **„— Keine —"**, 1/8, 1/4, 1/2, 1.

**Wichtig:** Taktart, Tonart, Tempo und Einheitslänge sind alle optional. Bei „Keine" wird die jeweilige Header-Zeile komplett weggelassen.

### 3. Sidebar — Register-Riegel

Ein animierter Umschalter (Pill gleitet) im Stil eines Schreibmaschinen-Hebels:

- **↓ Tief (Groß)**: fügt Großbuchstaben ein (`C D E F G A B`) = tiefe Oktave. **Standard.**
- **↑ Hoch (Klein)**: fügt Kleinbuchstaben ein (`c d e f g a b`) = hohe Oktave.

Das blaue Pill gleitet animiert zur aktiven Seite. Die Notentasten in der Toolbar zeigen sofort die passenden Groß-/Kleinbuchstaben (inkl. Oktav-Modifikatoren).

### 4. Sidebar — Notennamen-System

Drei Buttons, ändern **nur die Beschriftung** der Notentasten, nicht den ABC-Output:

- **A B C** (Deutsch/International) — Standard.
- **Do Re Mi** (Solfège: Do, Re, Mi, Fa, Sol, La, Si).
- **C / Do** (beides: Buchstabe groß, Solfège klein darunter).

Bei aktivem Hoch-Register wird auch das Solfège kleingeschrieben (`do re mi`).

### 5. Sidebar — Notenschlüssel

Drei Buttons mit Schlüssel-Glyphen: Violin (𝄞), Bass (𝄢), Alt (𝄡).

**Kritischer Punkt:** Der Schlüssel muss auch funktionieren, wenn keine Tonart gewählt ist. Lösung über die `K:`-Zeilen-Logik:
- Tonart + Schlüssel → `K:C bass`
- nur Tonart → `K:C`
- nur Schlüssel, keine Tonart → `K:none bass` (abcjs versteht `none` als leere Tonart!)
- nichts (Violin = Standard, keine Tonart) → gar keine `K:`-Zeile

### 6. Sidebar — Liedtext-Schalter (w:)

Ein Ein/Aus-Schalter für `w:`-Liedtextzeilen (silbenweise unter den Noten):

- **Aktivieren**: legt **leere** `w:`-Zeilen an.
  - Einstimmig: hinter jede Notenzeile, die noch keine `w:`-Zeile direkt darunter hat.
  - Mehrstimmig: eine `w:`-Zeile immer direkt nach der `[V:2]`-Zeile.
- **Deaktivieren**: entfernt **nur leere** `w:`-Zeilen (Regex `/^w:\s*$/`). Zeilen mit echtem Text bleiben erhalten — manuell eingetragene Werte dürfen NIE überschrieben werden.
- Beim Laden eines Liedes automatisch erkennen, ob bereits `w:`-Zeilen existieren, und den Schalter entsprechend setzen.
- Beim automatischen Neuaufbau im Mehrstimmen-Modus müssen vorhandene `w:`-Zeilen erhalten bleiben.

### 7. Mehrstimmigkeit (SATB)

**Stimmen-Leiste** ganz oben:

- Vier Buttons **S / A / T / B** (Sopran, Alt, Tenor, Bass) in vier deutlich unterscheidbaren Farben (z.B. Blau, Rot/Magenta, Grün, Orange). Der aktive Button ist farbig hervorgehoben.
- **Riegel „Einzelstimme / Mehrstimmig"**.
- Im Mehrstimmen-Modus ein **Stimmenzahl-Dropdown** (2 / 3 / 4 Stimmen).

Verhalten:
- Wählt man eine Stimme, **färbt sich die gesamte Eingabe-Toolbar** in der Farbe dieser Stimme (über ein `data-vtb`-Attribut + CSS), damit visuell klar ist, für welche Stimme man eingibt.
- Im Mehrstimmen-Modus erscheinen in der Sidebar **farbig markierte Einstellungs-Panels pro Stimme** (eigener Schlüssel + Register je Stimme).
- Eingegebene Noten landen im passenden `[V:n]`-Abschnitt.

**Erzeugte ABC-Struktur** (Beispiel 4-stimmig) — Stimmen paarweise in Notensystemen gruppiert:

```
%%staves (1 2) (3 4)
V:1 clef=treble stem=up name="Sopran"
V:2 clef=treble stem=down name="Alt"
V:3 clef=bass stem=up name="Tenor"
V:4 clef=bass stem=down name="Bass"
K:C
[V:1] c d e f | g a g f | e2 c2 |
[V:2] E F G A | B c B A | G2 E2 |
[V:3] G, A, B, C | D E D C | B,2 G,2 |
[V:4] C, C, G,, C, | G,, C, G,, C, | C,2 C,2 |
```

Gruppierung: 2 Stimmen → `(1 2)`; 3 → `(1 2) 3`; 4 → `(1 2) (3 4)`. Standard-Schlüssel/Register je Stimme: Sopran=Violin/tief, Alt=Violin/tief, Tenor=Alt-Schlüssel/hoch, Bass=Bass/tief; `stem` abwechselnd up/down pro Paar.

### 8. Notenvorschau — Interaktion

- **Live-Rendering**: bei jeder Änderung im ABC-Feld (debounced, ca. 250ms) neu rendern.
- **Klick auf eine Note**: spielt den Ton sofort ab (`ABCJS.synth.playEvent`) und markiert die Note im ABC-Textfeld (Selektion auf `startChar`/`endChar`).
- **Note mit der Maus hoch/runter ziehen**: ändert die Tonhöhe; der ABC-Text wird **live** angepasst, die Partitur springt sofort mit, der neue Ton klingt an.

**WICHTIG — Drag zuverlässig umsetzen:** Die native abcjs-`dragging:true`-Callback-Variante liefert `drag.step` in v6.4.3 nicht zuverlässig. Stattdessen **eigenes Drag** bauen:
1. Beim `mousedown` auf `#paper0` die geklickte Note per Bounding-Box-Trefferprüfung der `.abcjs-note`-Elemente finden.
2. Die DOM-Reihenfolge der `.abcjs-note`-Elemente 1:1 auf ein `noteIndex[]`-Array abbilden, das in derselben Traversierungsreihenfolge wie abcjs aufgebaut wird (lines → staff → voices → notes; nur `el_type==='note'` mit `startChar` und `midiPitches`).
3. Vertikale Mausbewegung tracken (ca. 9px pro diatonischem Schritt).
4. Note entlang einer **diatonischen Leiter** (`C,, D,, … b''`) verschieben, dabei Vorzeichen und Längen-Suffix erhalten (Regex `/^([_^=]*)([A-Ga-g][,']*)([\d\/]*)$/`).
5. Bei jeder Änderung sofort `renderAbc()` aufrufen und neuen Ton spielen.

Visuelles Feedback: Tooltip mit Notenname + MIDI-Nummer, Notenkopf färbt sich beim Ziehen orange.

### 9. Abspielleiste (Player)

Eingebaute `ABCJS.synth.SynthController` in ein `#player`-Div laden mit: Play/Pause, Loop, Neustart, Fortschrittsbalken, Tempo (%), Lautstärke. Ein Cursor (`.abcjs-cursor`) läuft während der Wiedergabe mit und markiert die aktuelle Note; die Selektion im Textfeld folgt. Die nötige `.abcjs-inline-audio`-CSS für den Player selbst mitliefern und auf die eigenen Theme-Variablen mappen.

### 10. ABC-Textfeld mit Syntax-Highlighting

- Editierbares `<textarea>` mit darüberliegendem Highlight-Overlay (gleiche Schrift/Position, transparenter Text im Textarea, eingefärbtes Overlay).
- Farben: Header-Zeilen (grün), Noten (blau), Vorzeichen (rot), Pausen (lila), Längen (orange), Akkorde/`[V:n]`/`%%` (teal).
- Per Checkbox „Farben" abschaltbar.
- Bidirektional synchron mit der Vorschau.

### 11. Theme — Hell/Dunkel

- Riegel oben rechts in der Toolbar (Sonne/Mond-Icons).
- Eigene CSS-Variablen für beide Themes (`[data-theme="light"]` / `[data-theme="dark"]`), inkl. Voice-Farben.
- Hintergrund leicht grau, Elemente abgestuft dunkler — alles muss unterscheidbar bleiben.
- **Dark Mode Noten**: Noten/Texte direkt in hellem Grau einfärben (`#E4E6EB`), Notenlinien dezenter. **NICHT** per `filter:invert()` lösen (verfälscht Markierungsfarben) — direkte `fill`/`stroke`-Overrides auf die SVG-Elemente. Markierungs-/Drag-Farben (blau/orange) müssen erhalten bleiben.

### 12. Vorlagen & eigene Lieder

- **Vorlagen**: Hänschen Klein, Tonleiter, Walzer, SATB-Beispiel (4-stimmig). Laden über eine wiederverwendbare `loadAbc(abc, mode, voices)`-Funktion, die alle Felder (Modus, Taktart, Tonart, Tempo, L:, Stimmenzahl, Liedtext-Status) aus dem ABC-Text zurücksetzt.
- **Eigene Lieder** (localStorage, Schlüssel z.B. `abcEditorSongs`):
  - „＋ Aktuelles Lied speichern" fragt per `prompt()` nach Namen, speichert `{name, abc, saved}`.
  - Liste gespeicherter Lieder mit Laden-Button (💾) und Löschen-Button (🗑).
  - Mit try/catch absichern; Hinweis, dass localStorage nur in der heruntergeladenen Datei funktioniert.

### 13. Drucken

- Button „🖨 Noten drucken" in der Aktionsleiste.
- Öffnet `window.open` mit **nur dem Noten-SVG**, erzwingt schwarze Noten, ruft automatisch `window.print()` auf.
- **Keinen** zusätzlichen Titel/Komponist-Header hinzufügen — Titel und Komponist stehen bereits im SVG (abcjs rendert `T:`/`C:` in die Partitur). Nur die Partitur drucken, so wie sie in der Vorschau aussieht.

### 14. Export & weitere Aktionen

- **⬇ MIDI** (`ABCJS.synth.getMidiFile` mit `midiOutputType:'encoded'`).
- **⬇ WAV** (`ABCJS.synth.CreateSynth().download()`).
- **⬇ ABC** (ABC-Text als `.abc`-Datei).
- **Neu** (leert das Stück, zurück in Einzelstimmen-Modus), **ABC ↗** (in Zwischenablage kopieren).
- **Zoom**-Slider (50–200 %) über `scale`-Option beim Rendern.
- Statistik-Badges: Anzahl Noten, Anzahl Takte.

### 15. Hilfe-Fenster

Button „❓ Hilfe & alle Funktionen" öffnet ein Overlay-Modal, das **jede** Funktion ausführlich erklärt:

- Stimmen, Noten-Eingabe, Oktave/Transponieren, Lied-Info, Notenvorschau, Speichern/Drucken/Export, Theme.
- Eine vollständige **ABC-Zeichenreferenz** (Header-Felder, Vorzeichen, Oktaven, Längen, Pausen, Takte, Wiederholungen, Akkorde, Triolen, `[V:n]`, `%%staves`).
- Ein eigener Abschnitt **„Liedtext schreiben (w:)"** mit Tabelle aller Trennzeichen und Beispielen:
  - Leerzeichen (trennt Silben), `-` (Silbentrennung über aufeinanderfolgende Noten), `_` (Melisma/Halten über nächste Note), `*` (leere Silbe/Note überspringen), `|` (zum nächsten Takt vorrücken), `~` (Wörter zu einer Silbe verbinden), `--` (sichtbarer Bindestrich), `\-` (echter Bindestrich im Wort).
  - Mehrere Strophen = mehrere `w:`-Zeilen untereinander.
  - Vollständiges Beispiel.
  - Unterschied `w:` (silbenweise ausgerichtet) vs. `W:` (Textblock am Liedende, nicht ausgerichtet).
- Schließen per ✕ oder Klick auf den Hintergrund.

## Header-Erzeugungs-Logik (zusammengefasst)

```
X:1
T:<Titel>
[C:<Komponist>]        — nur wenn ausgefüllt
[M:<Taktart>]          — nur wenn ≠ Keine
[L:<Einheitslänge>]    — nur wenn ≠ Keine
[Q:<Tempo>]            — nur wenn ≠ Keine
<K:-Zeile / %%staves+V:-Block>  — je nach Modus (siehe Schlüssel-Logik & Mehrstimmigkeit)
```

Der Body wird beim Header-Neuaufbau bewahrt; im Mehrstimmen-Modus werden die `[V:n]`-Inhalte und `w:`-Zeilen erhalten.

## Wichtige Fallstricke (unbedingt beachten)

1. **Unicode-Musikglyphen vermeiden** für Längen und Pause (𝅝 𝅗𝅥 𝅘𝅥𝅯 𝄽) — sie rendern oft als leere Kästchen. Textbeschriftungen verwenden (`1/4`, „Pause"). Die Schlüssel-Glyphen 𝄞 𝄢 𝄡 sind in Ordnung (mit Textlabel darunter).
2. **Drag manuell implementieren** (siehe Punkt 8) — die native abcjs-Drag-Callback ist unzuverlässig.
3. **Schlüssel über `K:none`** ermöglichen, wenn keine Tonart gesetzt ist.
4. **Dark Mode ohne `invert`-Filter** — direkte Farb-Overrides.
5. **Eigene CSS-Variablen** mitliefern, nicht auf Host-Stile verlassen (sonst weißer/kaputter Look beim Download).
6. **localStorage mit try/catch** umschließen.
7. **`w:`-Zeilen beim Deaktivieren nur löschen, wenn leer** — niemals Texteingaben überschreiben.
8. Beim **Drucken nur das SVG** ausgeben, keinen doppelten Titel.

## Ergebnis

Eine einzige `.html`-Datei (~85 KB), die offline lauffähig ist, von Laien bedienbar, und die ein- und mehrstimmige Notensätze erstellen, abspielen, bearbeiten (auch per Maus-Drag), mit Liedtext versehen, speichern, drucken und exportieren kann — mit Hell/Dunkel-Theme und vollständiger Hilfe.
