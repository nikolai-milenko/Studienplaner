
# 📚 Studienplaner (MVP)

Dies ist ein minimal funktionsfähiges Backend-Projekt (MVP) für eine Studienplaner-Anwendung.  
Das Projekt verwaltet Benutzer, Kurse, Aufgaben und Einreichungen für Studierende und Lehrkräfte.

## 🚀 Tech-Stack

- **Java 21**
- **Spring Boot 3.4.4**
- **Spring Data JPA**
- **PostgreSQL**
- **MapStruct**
- **Lombok**
- **Docker & Docker Compose**

## 📦 Projektstruktur

- **/user** — Verwaltung der Benutzer (Admin, Student, Lehrkraft)
- **/course** — Verwaltung der Kurse
- **/assignment** — Verwaltung der Aufgaben (Assignments)
- **/submission** — Verwaltung der Einreichungen (Submissions)

## 📌 Aktueller Stand

✅ CRUD für alle Kern-Entitäten  
✅ DTOs für saubere API-Datenübertragung  
✅ MapStruct-Mapping zwischen Entities und DTOs  
✅ RESTful API mit sinnvollen Endpunkten  
✅ PostgreSQL-Datenbank über Docker Compose integriert  
✅ Vorbereitung auf weitere Features (Validation, Auth, Tests)

## 🧩 Nächste Schritte (Roadmap)

- Datenvalidierung mit `javax.validation`
- Globale Fehlerbehandlung mit `@ControllerAdvice`
- API-Dokumentation mit Swagger / OpenAPI
- Unit-Tests und Integrationstests
- Fehler-Logging und Monitoring
- User-Authentifizierung (z.B. JWT)

## ⚙️ Docker

Das Projekt ist vollständig dockerisiert.  
Zum Starten der Anwendung:

```bash
docker-compose up --build
```

> Das Backend läuft anschließend auf: **http://localhost:8080**

## 💡 API-Endpunkte

### Benutzer
- `GET /users` — Alle Benutzer abrufen
- `POST /users` — Benutzer erstellen
- `DELETE /users/{id}` — Benutzer löschen
- `GET /users/{id}/courses` — Kurse eines Benutzers abrufen
- `GET /users/students` — Alle Studierenden abrufen

### Kurse
- `GET /courses` — Alle Kurse abrufen
- `POST /courses` — Kurs erstellen
- `DELETE /courses/{id}` — Kurs löschen
- `GET /courses/{id}/assignments` — Aufgaben des Kurses abrufen
- `GET /courses/{id}/students` — Teilnehmer des Kurses abrufen

### Aufgaben
- `GET /assignments` — Alle Aufgaben abrufen
- `POST /assignments` — Aufgabe erstellen
- `DELETE /assignments/{id}` — Aufgabe löschen

### Einreichungen
- `GET /submissions` — Alle Einreichungen abrufen
- `POST /submissions` — Einreichung erstellen
- `DELETE /submissions/{id}` — Einreichung löschen
- `PUT /submissions/{id}/status` — Status aktualisieren
- `PUT /submissions/{id}/grade` — Bewertung hinzufügen

## 🛠️ Vorbereitung

1. Stelle sicher, dass Docker installiert ist.
2. Baue das Projekt:
   ```bash
   ./mvnw clean package
   ```
3. Starte die Docker-Container:
   ```bash
   docker-compose up --build
   ```

---

## 👥 Autoren

- Nikolai Milenko — Hauptentwickler

---

## 🏁 Status

> ✅ MVP erreicht — bereit für nächste Ausbaustufen 🚀
