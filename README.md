![Build](https://github.com/nikolai-milenko/Studienplaner/actions/workflows/ci.yml/badge.svg?branch=dev)  
![codecov](https://codecov.io/gh/nikolai-milenko/Studienplaner/branch/dev/graph/badge.svg)

# 📚 Studienplaner

**Ihr smarter Assistent für Kursorganisation, Aufgabenverwaltung und Einreichungen – jetzt mit Sicherheits-Upgrade!**

Studienplaner ist unser leistungsstarkes Backend-MVP mit vollständiger CRUD‑API und JWT‑basiertem Authentifizierungs-Layer. Ideal für Studierende, Lehrende und Admins, die ihre Workflows digital vereinfachen möchten.

---

## 🚀 Technologien

- **Java 21** & **Spring Boot 3**
- **Spring Security** mit **JWT-Authentifizierung**
- **Spring Data JPA**
- **MapStruct** & **Lombok**
- **H2 In-Memory DB** (Tests)
- **Maven**
- **Swagger UI**
- **GitHub Actions** (CI/CD)
- **JaCoCo** & **Codecov** (Test Coverage)

---

## ✨ Features

1. **User Management**
   - Studierende, Lehrende & Admins mit feingranularen Rollen
   - Registrieren, Anmelden, Passwort-Reset

2. **Kursverwaltung**
   - Kurse anlegen, bearbeiten, löschen
   - Zuordnung von Lehrenden

3. **Aufgaben & Einreichungen**
   - Aufgaben für Kurse erstellen
   - Automatische Anlage von Submission‑Plätzen für alle Studierenden
   - Einreichung von Lösungen & Status‑Tracking
   - Bewertung durch Lehrende

4. **Sicherheit & Stabilität**
   - JWT‑Token für schnelle, sichere Sessions
   - Zentrale Sicherheitskonfiguration
   - Globales Exception‑Handling für konsistente Fehlermeldungen

---

## ✅ Projektstatus

- [x] Backend‑MVP
- [x] Validierte REST‑API (DTOs & Mapper)
- [x] Unit‑ & Integrationstests
- [x] CI/CD mit GitHub Actions
- [x] Test Coverage Reporting (JaCoCo & Codecov)
- [x] **Security‑Layer mit Spring Security & JWT**
- [ ] E‑Mail‑Benachrichtigungen (kommend)
- [ ] Frontend‑Integration (kommend)

---

## 🛠️ Installation & Nutzung

### 1️⃣ Lokale Entwicklung

1. **Java 21 installieren**  
   [Adoptium Temurin 21](https://adoptium.net/de/temurin/releases/?version=21)

2. **Repository klonen**
   ```bash
   git clone https://github.com/nikolai-milenko/Studienplaner.git
   cd Studienplaner
   ```

3. **Build & Tests**
   ```bash
   ./mvnw clean install
   ```

4. **Start with Docker**
   ```bash
   docker build -t studienplaner .
   docker-compose up --build
   ```

### 2️⃣ Nutzung ohne Java

1. **Docker Desktop installieren**  
   [Docker Desktop](https://www.docker.com/products/docker-desktop)

2. **Repository klonen**
   ```bash
   git clone https://github.com/nikolai-milenko/Studienplaner.git
   cd Studienplaner
   ```

3. **JAR aus Releases herunterladen** und ins Projektverzeichnis legen.

4. **Docker-Container starten**
   ```bash
   docker-compose up --build
   ```

---

Nach dem Start:
- **Anwendung:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html

---

## 🧪 Tests & Coverage

```bash
# Unit‑ & Integrationstests
mvn test

# Coverage‑Report lokal
mvn jacoco:report
# → target/site/jacoco/index.html
```

---

## 📂 Struktur

```
src/
 ├── main/
 │    ├── java/com/training/studienplaner/
 │    │     ├── assignment/
 │    │     ├── course/
 │    │     ├── security/
 │    │     ├── submission/
 │    │     └── user/
 │    └── resources/
 │          └── application.yml
 └── test/
      └── java/com/training/studienplaner/
```

---

## 📄 Lizenz

Private Entwicklung im Rahmen von Lern- und Demo-Zwecken.

---

> **Aktuell:** Stabiler MVP mit Sicherheits-Upgrade ✅  
> **Nächste Schritte:** E‑Mail‑Benachrichtigungen & Frontend-Integration  
