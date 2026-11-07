# ABC-Noten-Editor

Ein eigenständiger Noten-Editor als **einzelne HTML-Datei**. Man gibt Noten über eine Oberfläche ein (keine ABC- oder Programmierkenntnisse nötig), sieht sofort die Partitur, kann sie abspielen, drucken und als MIDI/WAV/ABC exportieren. Oberfläche dreisprachig: **Deutsch / English / Русский**.

Die Anwendung läuft vollständig **offline** im Browser (einzige Online-Abhängigkeit ist das abcjs-Skript per CDN; die kirchenslawische Schrift ist eingebettet).

## Aufrufen

Drei Wege:

1. **Online (GitHub Pages):** https://lanixx-hh.github.io/abcjs-online-editor/
2. **Lokal:** `index.html` herunterladen und per Doppelklick im Browser öffnen.
3. **Als Datei weitergeben:** Die einzelne `index.html` kopieren/verschicken — sie ist vollständig eigenständig.

> Hinweis: **Audio und Mikrofon** funktionieren nur in der echt geöffneten Datei bzw. über die Pages-URL — nicht in einer eingebetteten Vorschau.

## Bedienung

### Noten eingeben
- **Stimmen-Leiste (oben):** Stimme wählen (S/A/T/B), Einzel- oder Mehrstimmig.
- **Toolbar:** Noten-Tasten (C–H), Vorzeichen (♯/♭/♮), Notenlänge (Ganze … Sechzehntel), Taktstrich, Wiederholung, Zeilenumbruch, Löschen.
- **Oktave / Transposition:** Zähler in der Toolbar.
- **Notennamen-System:** Buchstaben, Solfège (Do-Re-Mi) oder kombiniert.

### Seitenspalte (Lied-Info & Einstellungen)
- **Lied-Info:** Titel, Komponist, Taktart, Tonart, Tempo, Einheitslänge (alle optional).
- **Notenschlüssel:** Violin / Bass / Alt — pro Stimme.
- **Register (Oktave):** Hoch/Tief — verschiebt die Stimme um eine Oktave.
- **Liedtext (`w:`):** Schalter zum Unterlegen von Text unter die Noten.

### Mehrstimmigkeit (SATB)
- Umschalten auf **Mehrstimmig**, Stimmenzahl 2/3/4.
- Je Stimme eigener Schlüssel und Register.
- **Stimmen-Namen frei änderbar** (Feld im jeweiligen Stimmen-Block) — erscheinen als Beschriftung in der Partitur.

### Singen statt tippen (Mikrofon → Noten)
- **🎤-Taste** in der Stimmen-Leiste: Gesungene/gesummte Töne werden erkannt und als Noten eingefügt (einstimmig).
- **⚙ Einstellungen** daneben: Live-Anzeige (erkannte Note, Frequenz, Pegel) plus Regler für Empfindlichkeit, Reaktion, Glättung, Stimmlage und Browser-Rauschunterdrückung.
- Tipp: Töne klar absetzen (kurze Pause zwischen den Noten), ruhige Umgebung.

### Partitur-Vorschau
- **Klick auf eine Note:** spielt sie.
- **Note ziehen (↑/↓):** ändert die Tonhöhe.
- Live-Aktualisierung bei jeder Änderung.

### Abspielen & Text bearbeiten
- **Abspielleiste:** Wiedergabe mit mitlaufendem Cursor.
- **ABC-Textfeld:** direkte Bearbeitung des ABC-Codes mit Syntax-Hervorhebung (umschaltbar).

### Schriftarten
- Auswahl mehrerer Schriften für den Noten-/Titelbereich, inkl. eingebetteter **kirchenslawischer Schrift (Ponomar)** für kyrillische/altslawische Titel.

### Vorlagen & eigene Lieder
- **Vorlagen:** Hänschen Klein, Tonleiter, Walzer, SATB.
- **Speichern/Laden:** eigene Lieder im Browser ablegen (localStorage).

### Drucken & Export
- **Drucken:** sauber umgebrochene Partitur im A4-Format (Noten brechen auf die Seitenbreite um, keine abgeschnittenen Systeme).
- **Export:** MIDI, WAV, ABC-Datei.

### Darstellung
- **Theme:** Hell/Dunkel.
- **Zoom:** 50–200 %.

## Technik (kurz)

- Eine HTML-Datei, Vanilla-JavaScript, kein Build-Schritt.
- Rendering & Audio über [abcjs](https://abcjs.net) (per CDN).
- Speichern nur via `localStorage` (mit Fallback, falls in der Umgebung blockiert).

## Deployment (GitHub Pages)

Der Stand wird bei jedem Push auf `main` automatisch per GitHub Actions nach GitHub Pages veröffentlicht (`.github/workflows/pages.yml`). Einmalig nötig: **Settings → Pages → Source: „GitHub Actions"**.
