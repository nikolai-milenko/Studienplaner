![Build](https://github.com/nikolai-milenko/Studienplaner/actions/workflows/ci.yml/badge.svg?branch=dev)
![codecov](https://codecov.io/gh/nikolai-milenko/Studienplaner/branch/dev/graph/badge.svg)

# 📚 Studienplaner

Studienplaner ist eine Anwendung zur Organisation von Kursen, Aufgaben und Einreichungen für Studierende und Lehrende.  
Das Projekt ist ein Backend-MVP mit vollständigen CRUD-Operationen und validierter REST-API.

## 🚀 Technologien

- Java 21
- Spring Boot 3
- Spring Data JPA
- MapStruct
- Lombok
- H2 In-Memory-Datenbank (für Tests)
- Maven
- Swagger UI
- GitHub Actions (CI/CD)
- JaCoCo (Test Coverage)
- Codecov (Coverage Reporting)

## 🔧 Funktionen

- Verwaltung von Nutzern (Studierende, Lehrende, Admins)
- Verwaltung von Kursen und deren Zuordnung zu Lehrenden
- Erstellung und Verwaltung von Aufgaben für Kurse
- Automatische Generierung von Submission-Plätzen für alle Studierenden beim Erstellen einer Aufgabe
- Abgabe von Lösungen durch Studierende
- Bewertungen und Statusverwaltung der Einreichungen

## ✅ Projektstatus

- [x] Backend MVP fertiggestellt
- [x] DTOs & Mapper implementiert
- [x] Validierung der Anfrage-Daten
- [x] Unit-Tests für Repository-Schicht (User & Submission)
- [x] Swagger UI dokumentiert
- [x] CI/CD eingerichtet (Build, Tests, Coverage)
- [x] Code Coverage Reporting mit Codecov
- [x] Integrationstests
- [ ] Security (Spring Security) noch ausstehend
- [ ] Eventuelle Erweiterung auf E-Mail-Benachrichtigungen
- [ ] Frontend-Integration



## 🛠️ Installation & Verwendung

### Option 1: Lokale Installation (Java muss installiert sein)

1. **Java installieren (falls nicht vorhanden):**
   - [Java 21 herunterladen (Eclipse Temurin)](https://adoptium.net/de/temurin/releases/?version=21)

2. **Projekt klonen:**
   ```bash
   git clone https://github.com/nikolai-milenko/Studienplaner.git
   cd Studienplaner
   ```

3. **Build ausführen:**
   ```bash
   ./mvnw clean install
   ```

4. **Docker-Container aus lokal gebautem Jar starten:**
   ```bash
   docker build -t studienplaner .
   docker-compose up --build
   ```

### Option 2: Mit Docker und heruntergeladenem Jar (Java wird nicht benötigt)

1. **Docker installieren (falls nicht vorhanden):**
   - [Docker Desktop herunterladen](https://www.docker.com/products/docker-desktop)

2. **Projekt klonen:**
   ```bash
   git clone https://github.com/nikolai-milenko/Studienplaner.git
   cd Studienplaner
   ```

3. **Jar-Datei herunterladen:**
   - Lade die neueste `Studienplaner-0.0.1-SNAPSHOT.jar` aus den GitHub Actions-Artefakten oder GitHub Releases herunter.
   - Lege die Datei im Root-Verzeichnis des Projekts ab.

4. **Docker-Container starten:**
   ```bash
   docker-compose up --build
   ```

---

Nach dem Start erreichst du die Anwendung unter: [http://localhost:8080](http://localhost:8080)  
Die Swagger-UI findest du hier: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)



## 🧪 Tests

Tests lokal ausführen:

```bash
mvn test
```

Test Coverage Report lokal generieren:

```bash
mvn jacoco:report
```

Report befindet sich danach unter:
```
target/site/jacoco/index.html
```

## 🖥️ CI/CD Pipeline

- Build und Test laufen automatisch bei jedem Push oder Pull Request auf `dev` und `master`.
- Code Coverage wird mit [Codecov](https://app.codecov.io) erfasst und aktualisiert.

## 📂 Ordnerstruktur

```
src/
 ├── main/
 │    ├── java/com/training/studienplaner/
 │    │     ├── assignment/
 │    │     ├── course/
 │    │     ├── submission/
 │    │     └── user/
 │    └── resources/
 │          └── application.yml
 └── test/
      └── java/com/training/studienplaner/
```

## 📄 Lizenz

Private Entwicklung im Rahmen von Lernzwecken.

---

> Aktueller Stand: Stabiler MVP ✅
>
> Ziel: Weiterentwicklung zur produktionsreifen Applikation
