# Dex.IO

Dex.IO ist eine JavaFX-basierte Anwendung, die Daten aus der Pokémon API ([PokeAPI](https://pokeapi.co/)) abruft und anzeigt. Die Anwendung bietet Funktionen wie eine Pokémon-Liste, Detailinformationen und eine Exportfunktion, um Pokémon-Daten als JSON-Datei zu speichern.

---

## Features
- **Anzeige einer Liste aller Pokémon**
- **Detailansicht für ausgewählte Pokémon** mit:
  - Name
  - Typen
  - Bild
  - Beschreibung
- **Export der Daten** des ausgewählten Pokémon als JSON-Datei mit dem Pokémon-Namen direkt auf den Desktop

---

## Installation

### Voraussetzungen
- **Java 21** oder neuer
- **Gradle** (optional, falls nicht im Projekt enthalten)

### Schritte zur Installation
1. **Projekt klonen oder herunterladen:**
    ```bash
    git clone https://github.com/Richy2811/Dex.IO
    ```

2. **Abhängigkeiten sicherstellen:**
    - **JavaFX**: Für die GUI-Funktionalität
    - **Jackson**: Für die JSON-Exportfunktion

3. **Anwendung starten:**
    ```bash
    ./gradlew run
    ```

---


## Geplante Features (Roadmap)
- **Erweiterung der Exportdaten**:
  - Angriff, Verteidigung und Fähigkeiten
- **Team-Building-Funktion**:
  - Erstellen von Pokémon-Teams
- **Typen-Analyse**:
  - Effektive Kampfstrategien basierend auf Typen

---

## Verwendung der Anwendung
### Pokémon-Exportfunktion
- Wähle ein Pokémon aus der Liste.
- Klicke auf "Export".
- Eine JSON-Datei mit dem Namen des Pokémon wird direkt auf deinem Desktop gespeichert.

### Entwicklung und Anpassung
Falls du eigene Features hinzufügen möchtest, stelle sicher, dass du mit **JavaFX** und **JSON-Handling** vertraut bist.

---

## Hinweise für Entwickler
- **Java-Version:** Mindestens **Java 21**.
- **Kompatibilität:** Die Anwendung ist vollständig modularisiert, mit einem Fokus auf einfache Erweiterbarkeit.
- **Dokumentation:** Eine ausführliche JavaDoc-Dokumentation ist im Projekt enthalten.

---

## Beiträge
Beiträge sind willkommen! Falls du Ideen hast oder Fehler findest:
- Öffne ein **Issue**.
- Erstelle einen **Pull Request**.

---

## Credits
- **Datenquelle:** [PokeAPI](https://pokeapi.co/)

---


