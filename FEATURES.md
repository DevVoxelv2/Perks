# NitroPerks v2.0.0 - Erweiterte Features

## 🎯 Überblick der Verbesserungen

Das NitroPerks-System wurde umfassend erweitert und verbessert mit **vollständiger Unterstützung für Minecraft 1.20 - 1.21.8**. Hier ist eine Übersicht aller neuen Features und Verbesserungen:

## 🔧 Versionskompatibilität

### ✅ **Unterstützte Versionen**
- **Minecraft 1.20.x** - Alle Unterversionen vollständig unterstützt
- **Minecraft 1.21.0 - 1.21.8** - Vollständige Kompatibilität
- **Java 17+** - Erforderlich für optimale Performance
- **Spigot/Paper** - Empfohlen für beste Leistung

### 🔄 **Automatische Anpassung**
Das Plugin erkennt automatisch Ihre Serverversion und:
- ✅ Verwendet kompatible Sounds, Partikel und Materialien
- ✅ Behandelt API-Unterschiede zwischen Versionen elegant
- ✅ Bietet Fallbacks für ältere/neuere Versionen
- ✅ Protokolliert Kompatibilitätsinformationen beim Start

### 🛠️ **Technische Verbesserungen**
- **VersionCompatibility-Klasse** - Automatische API-Anpassung
- **Intelligente Fallbacks** - Sichere Alternativen bei Inkompatibilitäten  
- **Performance-Optimierung** - Speziell für neuere MC-Versionen
- **Folia-Unterstützung** - Bereit für moderne Server-Software

## 🎨 Erweiterte GUI-Systeme

### 1. Kategorisierte Hauptmenü
- **Kategorien**: Combat, Utility, Movement, Survival, Mining
- **Visuell ansprechend**: Farbkodierte Kategorien mit benutzerdefinierten Icons
- **Statistiken**: Zeigt besessene Perks pro Kategorie an
- **Navigation**: Intuitive Menüführung mit Back-Buttons

### 2. Verbesserte Shop-Interface
- **Preisanzeige**: Formatierte Preise mit Währungssymbolen
- **Erschwinglichkeit**: Visuelle Indikatoren ob Spieler sich Perk leisten kann
- **Status-Indikatoren**: Klar erkennbar ob Perk bereits besessen wird
- **Detaillierte Beschreibungen**: Jeder Perk hat eine ausführliche Beschreibung

### 3. Perk-Übersicht
- **Status-Übersicht**: Aktive vs. inaktive Perks
- **Toggle-Funktionalität**: Ein-/Ausschalten von Perks per Klick
- **Visuelle Feedback**: Sofortiges visuelles Feedback bei Änderungen

## 🔧 Admin-Management-System

### 1. Admin-Panel
- **Hauptmenü**: Zentraler Zugriff auf alle Admin-Funktionen
- **Spielerverwaltung**: Verwalte Perks für alle Spieler
- **Konfiguration**: Direkte Preisanpassungen über GUI
- **Statistiken**: Server-weite Perk-Nutzungsstatistiken

### 2. Spieler-Management
- **Live-Übersicht**: Alle Online-Spieler mit ihren Perk-Status
- **Direkter Zugriff**: Klick auf Spieler öffnet deren Perk-Management
- **Bulk-Operationen**: Schnelle Verwaltung mehrerer Spieler

### 3. Perk-Konfiguration
- **Preisanpassung**: Einfache Preisänderungen über GUI
- **Permission-Übersicht**: Alle Permissions auf einen Blick
- **Kategorie-Zuordnung**: Übersichtliche Kategorie-Anzeige

## 🎭 Effekte-System

### 1. Visuelle Effekte
- **Partikel-Effekte**: Verschiedene Partikel für verschiedene Aktionen
- **Perk-Aktivierung**: Spezielle Effekte beim Ein-/Ausschalten
- **Kauf-Erfolg**: Feuerwerk-Effekte bei erfolgreichem Kauf
- **Aura-Effekte**: Kontinuierliche Partikel für aktive Perks

### 2. Audio-Feedback
- **Sound-Effekte**: Passende Sounds für alle Aktionen
- **Menü-Navigation**: Klick-Sounds für bessere UX
- **Erfolgs-Sounds**: Bestätigungstöne für Aktionen
- **Fehler-Sounds**: Warntöne bei Fehlern

### 3. Titel & Action Bar
- **Titel-Nachrichten**: Große Bestätigungen für wichtige Aktionen
- **Action Bar**: Schnelle Status-Updates
- **Farbkodierung**: Intuitive Farbschemata für verschiedene Status

## 💾 Erweiterte Datenbank-Persistenz

### 1. Asynchrone Operationen
- **Performance**: Alle DB-Operationen laufen asynchron
- **Caching**: Intelligentes Caching für bessere Performance
- **Auto-Reconnect**: Automatische Wiederverbindung bei Verbindungsfehlern

### 2. Erweiterte Tabellen
- **player_perks**: Detaillierte Perk-Daten mit Leveln und Nutzungsstatistiken
- **player_stats**: Spieler-Statistiken und Ausgaben-Tracking
- **Timestamps**: Vollständige Zeitstempel für alle Aktionen

### 3. Daten-Synchronisation
- **Join/Quit Events**: Automatisches Laden/Speichern bei Spieler-Events
- **Real-time Updates**: Sofortige Synchronisation aller Änderungen
- **Backup-Mechanismen**: Sichere Datenspeicherung vor Plugin-Shutdown

## 📊 Statistik-System

### 1. Spieler-Statistiken
- **Besessene Perks**: Anzahl aller freigeschalteten Perks
- **Aktive Perks**: Derzeit aktive Perks
- **Ausgaben-Tracking**: Gesamtausgaben für Perks
- **Beitritts-/Aktivitätsdaten**: Wann Spieler beigetreten/zuletzt gesehen

### 2. Server-Statistiken
- **Beliebteste Perks**: Welche Perks am meisten genutzt werden
- **Spieler-Übersicht**: Gesamtzahl Spieler und Online-Status
- **Wirtschafts-Daten**: Gesamtausgaben im Perk-System

## 🎯 Kategorie-System

### 1. Perk-Kategorien
- **Combat**: Kampf-orientierte Perks
- **Utility**: Nützliche Alltags-Perks  
- **Movement**: Bewegungs-Verbesserungen
- **Survival**: Überlebens-Hilfen
- **Mining**: Abbau-Verbesserungen

### 2. Kategorisierte Navigation
- **Gefilterte Ansichten**: Nur Perks der gewählten Kategorie
- **Kategorie-Statistiken**: Besitz-Status pro Kategorie
- **Intuitive Icons**: Passende Icons für jede Kategorie

## 🔧 Konfiguration

### 1. Erweiterte messages.yml
- **GUI-Nachrichten**: Alle GUI-Texte konfigurierbar
- **Sound-Einstellungen**: Ein-/Ausschalten und Anpassung von Sounds
- **Partikel-Einstellungen**: Konfiguration aller visuellen Effekte

### 2. Verbesserte menus.yml
- **Template-System**: Wiederverwendbare GUI-Elemente
- **Flexible Layouts**: Anpassbare Menü-Strukturen
- **Farbkonfiguration**: Vollständige Farbkontrolle

## 🚀 Performance-Optimierungen

### 1. Asynchrone Verarbeitung
- **Datenbank-Operationen**: Alle DB-Zugriffe non-blocking
- **Effekt-Rendering**: Optimierte Partikel-Darstellung
- **Menu-Loading**: Schnelle Menü-Generierung

### 2. Caching-System
- **Spieler-Daten**: Intelligentes Caching für häufig abgerufene Daten
- **Menü-Templates**: Vorgenerierte Menü-Elemente
- **Konfigurations-Cache**: Schneller Zugriff auf Einstellungen

## 🛡️ Sicherheits-Features

### 1. Permission-System
- **Granulare Kontrolle**: Detaillierte Berechtigungen für alle Features
- **Admin-Schutz**: Sichere Admin-Funktionen
- **Fallback-Mechanismen**: Sichere Standardwerte bei fehlenden Permissions

### 2. Validierung
- **Input-Validation**: Sichere Verarbeitung aller Benutzereingaben
- **Error-Handling**: Robuste Fehlerbehandlung
- **Logging**: Detaillierte Logs für Debugging

## 📝 Kommandos

### Spieler-Kommandos
- `/perks` - Öffnet das Hauptmenü
- `/perkshop` - Direkter Zugang zum Shop (über Hauptmenü)

### Admin-Kommandos
- `/perks admin` - Öffnet das Admin-Panel
- `/perks add <player> <perk>` - Gibt einem Spieler einen Perk
- `/perks remove <player> <perk>` - Entfernt einen Perk von einem Spieler
- `/perks setprice <perk> <price>` - Setzt den Preis für einen Perk
- `/perks reload` - Lädt die Konfiguration neu

## 🎮 Benutzerfreundlichkeit

### 1. Intuitive Navigation
- **Breadcrumb-Navigation**: Immer klar wo man sich befindet
- **Einheitliche Icons**: Konsistente Symbolik durch alle Menüs
- **Schneller Zugriff**: Wenige Klicks zu allen Funktionen

### 2. Feedback-Systeme
- **Sofortige Bestätigung**: Jede Aktion wird bestätigt
- **Fehler-Meldungen**: Klare Kommunikation bei Problemen  
- **Hilfe-Texte**: Beschreibende Lore-Texte für alle Elemente

### 3. Responsives Design
- **Verschiedene Bildschirmgrößen**: Optimiert für alle Minecraft-Clients
- **Konsistente Layouts**: Einheitliches Design-System
- **Barrierefreiheit**: Klare Kontraste und lesbare Texte

## 🔄 Zukunfts-Features (Geplant)

- **Perk-Level-System**: Verschiedene Stufen für Perks
- **Erweiterte Statistiken**: Detailliertere Nutzungsanalysen
- **Belohnungssystem**: Achievements für Perk-Nutzung
- **API-Integration**: Plugin-API für Entwickler

---

**Dieses erweiterte NitroPerks-System bietet eine vollständige, professionelle Lösung für Server-Perks mit modernster GUI, umfassenden Admin-Tools und optimaler Performance.**
