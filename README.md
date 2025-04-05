
# 📚 Studienplaner

Ein modernes Backend-System zur Verwaltung von Kursen, Aufgaben und Abgaben für Studierende und Lehrkräfte.  
Das Projekt wird aktuell entwickelt und befindet sich in der Phase der Implementierung von Mappings und API-Anbindung.

## 🚀 Aktueller Entwicklungsstand

- ✅ Projektstruktur erstellt (Entities, Services, DTOs)
- ✅ Datenbankintegration mit PostgreSQL und Docker-Compose
- ✅ Serviceschicht für Business-Logik implementiert
- ✅ Alle DTOs fertiggestellt
- ✅ Mappings zwischen Entities und DTOs (MapStruct)
- ⏳ API-Endpunkte folgen

## ⚙️ Technologien

- **Java 21**
- **Spring Boot 3.4**
- **Spring Data JPA**
- **MapStruct** für Mapping
- **PostgreSQL** als Datenbank
- **Docker & Docker Compose** für Umgebung
- **Lombok** für sauberen Code

## 📦 Projektstruktur

```
studienplaner/
├── src/main/java/com/training/studienplaner/
│   ├── assignment/
│   ├── course/
│   ├── submission/
│   └── user/
├── src/main/resources/
│   └── application.properties
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 🐳 Docker

Starte das Projekt lokal mit Docker-Compose:

```bash
docker-compose up --build
```

- Postgres läuft auf Port **5432**
- pgAdmin ist verfügbar auf Port **5050**

## 🛠️ Geplante nächste Schritte

- 🔲 Implementierung der REST-Controller
- 🔲 Integrierte API-Dokumentation mit Swagger/OpenAPI
- 🔲 Automatisierte Tests hinzufügen

## 👨‍💻 Entwickler

> Dieses Projekt wird als Lernprojekt erstellt, mit Fokus auf saubere Architektur, klare API-Struktur und Best Practices in Spring Boot.

---

*Letztes Update: April 2025*
