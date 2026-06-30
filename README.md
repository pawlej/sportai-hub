# SportAI Hub Mini

SportAI Hub Mini jest aplikacją klient-serwer służącą do zarządzania działalnością koła naukowego zajmującego się sztuczną inteligencją w sporcie.

System umożliwia zarządzanie członkami, projektami i zadaniami, a także prezentuje podstawowe statystyki działalności koła. Aplikacja posiada mechanizm historii aktywności, automatyczne logowanie zdarzeń przez Spring AOP oraz aktualizacje w czasie rzeczywistym z wykorzystaniem WebSocket.

## Funkcjonalności

- zarządzanie członkami koła,
- dodawanie, edytowanie i usuwanie członków,
- zarządzanie projektami,
- dodawanie, edytowanie i usuwanie projektów,
- przypisywanie liderów do projektów,
- zarządzanie zadaniami projektowymi,
- przypisywanie członków do zadań,
- zmiana statusów projektów i zadań,
- dashboard prezentujący podstawowe statystyki,
- historia ostatnich aktywności,
- aktualizacje w czasie rzeczywistym przez WebSocket,
- automatyczne logowanie zdarzeń biznesowych przez Spring AOP,
- walidacja danych wejściowych,
- obsługa błędów REST API,
- trwałe przechowywanie danych w PostgreSQL,
- uruchamianie całego systemu przez Docker Compose.

## Architektura systemu

Aplikacja została wykonana w architekturze klient-serwer.

Część kliencka została przygotowana jako aplikacja SPA w technologii React. Komunikuje się z backendem przez REST API oraz WebSocket.

Część serwerowa została wykonana w technologii Spring Boot. Logika aplikacji została podzielona na kontrolery, serwisy, repozytoria, encje JPA, obiekty DTO, konfigurację, obsługę wyjątków oraz mechanizmy AOP.

Dane przechowywane są w bazie PostgreSQL uruchamianej w osobnym kontenerze Docker.

Frontend działa za pośrednictwem serwera Nginx, który udostępnia aplikację React i pełni funkcję reverse proxy dla żądań REST oraz połączeń WebSocket.

## Technologie

### Backend

- Java 17/21,
- Spring Boot,
- Spring Web,
- Spring Data JPA,
- Spring AOP,
- Spring WebSocket,
- Spring Validation,
- Spring Boot Actuator,
- Hibernate,
- PostgreSQL,
- Maven.

### Frontend

- React,
- Vite,
- JavaScript,
- Axios,
- React Router,
- STOMP,
- HTML,
- CSS,
- Nginx.

### Testy

- JUnit,
- Spring Boot Test,
- MockMvc,
- H2.

### Infrastruktura

- Docker,
- Docker Compose,
- Nginx,
- Debian 12,
- maszyna wirtualna.

## Struktura projektu

```text
sportai-hub/
├── backend/
│   └── backend/
│       ├── src/
│       ├── pom.xml
│       ├── mvnw
│       ├── mvnw.cmd
│       └── Dockerfile
├── frontend/
│   ├── src/
│   ├── package.json
│   ├── vite.config.js
│   ├── nginx.conf
│   └── Dockerfile
├── dokumentacja/
│   ├── diagrams/
│   ├── javadoc/
│   ├── screenshots/
│   └── README_DOKUMENTACJA.md
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

## Model danych

Aplikacja wykorzystuje następujące główne encje:

### Member

Reprezentuje członka koła naukowego.

Przykładowe pola:

- identyfikator,
- imię,
- nazwisko,
- adres e-mail,
- specjalizacja,
- status aktywności.

### Project

Reprezentuje projekt realizowany w ramach koła.

Przykładowe pola:

- identyfikator,
- nazwa,
- opis,
- status,
- lider projektu.

### ProjectTask

Reprezentuje zadanie należące do projektu.

Przykładowe pola:

- identyfikator,
- tytuł,
- opis,
- status,
- projekt,
- przypisany członek.

### ActivityLog

Reprezentuje wpis w historii aktywności.

Przykładowe pola:

- identyfikator,
- typ zdarzenia,
- opis,
- data i czas utworzenia.

## REST API

Backend udostępnia następujące główne endpointy.

### Członkowie

```text
GET    /api/members
GET    /api/members/{id}
POST   /api/members
PUT    /api/members/{id}
DELETE /api/members/{id}
```

### Projekty

```text
GET    /api/projects
GET    /api/projects/{id}
POST   /api/projects
PUT    /api/projects/{id}
DELETE /api/projects/{id}
```

### Zadania

```text
GET    /api/tasks
GET    /api/tasks/{id}
POST   /api/tasks
PUT    /api/tasks/{id}
DELETE /api/tasks/{id}
PATCH  /api/tasks/{id}/status
```

### Dashboard

```text
GET /api/dashboard
```

### Historia aktywności

```text
GET /api/activities
```

### Health check

```text
GET /actuator/health
```

## WebSocket

Aplikacja wykorzystuje WebSocket oraz protokół STOMP do przesyłania zdarzeń w czasie rzeczywistym.

Endpoint połączenia:

```text
/ws
```

Kanał subskrypcji:

```text
/topic/activity
```

Po wykonaniu operacji biznesowej, na przykład dodaniu członka lub utworzeniu projektu, backend publikuje zdarzenie. Frontend odbiera je i aktualizuje historię aktywności bez konieczności ręcznego odświeżania strony.

## Spring AOP

Spring AOP jest wykorzystywany do automatycznego rejestrowania zdarzeń biznesowych.

Wybrane metody warstwy serwisowej są oznaczane adnotacją:

```text
@LogActivity
```

Aspekt:

```text
ActivityLogAspect
```

przechwytuje wykonanie oznaczonych metod, zapisuje wpis w historii aktywności oraz publikuje zdarzenie przez WebSocket.

Dzięki temu logika rejestrowania zdarzeń jest oddzielona od właściwej logiki biznesowej.

## Wymagania lokalne

Do uruchomienia aplikacji potrzebne są:

- Docker Desktop lub Docker Engine,
- Docker Compose,
- Git.

Opcjonalnie, do uruchamiania backendu poza kontenerem:

- Java 17 lub nowsza,
- Maven lub Maven Wrapper.

Opcjonalnie, do uruchamiania frontendu poza kontenerem:

- Node.js,
- npm.

## Konfiguracja środowiska

W głównym katalogu projektu znajduje się plik:

```text
.env.example
```

Należy skopiować go jako:

```text
.env
```

Przykładowa zawartość:

```env
POSTGRES_DB=sportaihub
POSTGRES_USER=sportaihub
POSTGRES_PASSWORD=change_me
APP_PORT=8085
```

Hasło:

```text
change_me
```

należy zmienić na własne.

Plik `.env` nie powinien być umieszczany w repozytorium.

## Uruchomienie lokalne przez Docker Compose

Przejdź do głównego katalogu projektu:

```powershell
cd C:\Users\plejc\IdeaProjects\sportai-hub
```

Skopiuj plik konfiguracyjny:

```powershell
Copy-Item .env.example .env
```

Uzupełnij wartości w `.env`.

Następnie uruchom aplikację:

```powershell
docker compose up -d --build
```

Po uruchomieniu aplikacja będzie dostępna pod adresem:

```text
http://localhost:8085
```

Health check backendu:

```text
http://localhost:8085/actuator/health
```

Przykładowa poprawna odpowiedź:

```json
{
  "status": "UP"
}
```

## Sprawdzenie kontenerów

Aby sprawdzić stan kontenerów, wykonaj:

```powershell
docker compose ps
```

Powinny działać trzy kontenery:

```text
sportaihub-frontend
sportaihub-backend
sportaihub-db
```

## Logi aplikacji

Logi wszystkich kontenerów:

```powershell
docker compose logs
```

Logi backendu:

```powershell
docker compose logs backend
```

Logi frontendu:

```powershell
docker compose logs frontend
```

Logi bazy danych:

```powershell
docker compose logs db
```

Śledzenie logów na żywo:

```powershell
docker compose logs -f
```

## Zatrzymanie aplikacji

```powershell
docker compose down
```

Polecenie zatrzymuje i usuwa kontenery, ale nie usuwa danych z wolumenu PostgreSQL.

## Usunięcie danych bazy

Aby usunąć również wolumen z danymi:

```powershell
docker compose down -v
```

Uwaga: to polecenie trwale usuwa dane zapisane w bazie.

## Uruchomienie backendu bez Dockera

Przejdź do katalogu backendu:

```powershell
cd backend\backend
```

Ustaw zmienne środowiskowe lub uruchom lokalną bazę PostgreSQL.

Następnie uruchom:

```powershell
.\mvnw.cmd spring-boot:run
```

Backend będzie domyślnie dostępny pod adresem:

```text
http://localhost:8080
```

## Uruchomienie frontendu bez Dockera

Przejdź do katalogu frontendu:

```powershell
cd frontend
```

Zainstaluj zależności:

```powershell
npm install
```

Uruchom aplikację developerską:

```powershell
npm run dev
```

Frontend będzie domyślnie dostępny pod adresem:

```text
http://localhost:5173
```

## Budowanie backendu

Przejdź do katalogu:

```powershell
cd backend\backend
```

Uruchom:

```powershell
.\mvnw.cmd clean package "-DskipTests"
```

Wynikowy plik JAR zostanie utworzony w katalogu:

```text
target
```

## Budowanie frontendu

Przejdź do katalogu:

```powershell
cd frontend
```

Uruchom:

```powershell
npm install
npm run build
```

Gotowy frontend produkcyjny zostanie utworzony w katalogu:

```text
dist
```

## Testy backendu

Testy backendu wykorzystują:

- JUnit,
- Spring Boot Test,
- MockMvc,
- H2.

Uruchomienie testów:

```powershell
cd backend\backend
.\mvnw.cmd test
```

Przykładowe testowane scenariusze:

- uruchomienie kontekstu Spring,
- dodanie członka,
- odrzucenie zduplikowanego adresu e-mail,
- utworzenie projektu,
- utworzenie zadania,
- zmiana statusu zadania,
- blokada usunięcia projektu posiadającego zadania,
- zapis historii aktywności przez Spring AOP.

## JavaDoc

Dokumentacja JavaDoc jest generowana dla części serwerowej.

Przejdź do katalogu backendu:

```powershell
cd backend\backend
```

Ustaw `JAVA_HOME`, jeżeli nie jest skonfigurowane:

```powershell
$env:JAVA_HOME="C:\Users\plejc\.jdks\openjdk-21.0.2"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

Następnie wykonaj:

```powershell
.\mvnw.cmd javadoc:javadoc
```

Wygenerowana dokumentacja będzie dostępna w katalogu:

```text
backend\backend\target\site\apidocs
```

Strona główna dokumentacji:

```text
backend\backend\target\site\apidocs\index.html
```

Kopia JavaDoc dołączona do projektu znajduje się również w katalogu:

```text
dokumentacja\javadoc
```

## Wdrożona aplikacja

Aplikacja została wdrożona na maszynie wirtualnej:

```text
http://172.20.41.28:8085
```

Adres jest dostępny z sieci uczelnianej.

Serwer wykorzystuje system Debian 12 oraz Docker Compose.

## Dostęp przez tunel SSH

Aby uzyskać dostęp do aplikacji spoza sieci uczelnianej, można utworzyć tunel przez serwer Taurus.

W PowerShellu uruchom:

```powershell
ssh -N -L 8085:172.20.41.28:8085 2lejczak@taurus.fis.agh.edu.pl
```

Po uruchomieniu tunelu aplikacja jest dostępna lokalnie pod adresem:

```text
http://localhost:8085
```

Okno terminala z aktywnym tunelem musi pozostać otwarte.

## Wdrożenie na maszynie wirtualnej

Po połączeniu z maszyną wirtualną należy przejść do katalogu projektu:

```bash
cd ~/sportai-hub
```

Pobrać aktualne zmiany:

```bash
git pull
```

Uruchomić lub przebudować aplikację:

```bash
docker compose up -d --build
```

Sprawdzić kontenery:

```bash
docker compose ps
```

Sprawdzić endpoint zdrowia:

```bash
curl http://localhost:8085/actuator/health
```

Sprawdzić REST API:

```bash
curl http://localhost:8085/api/members
```

## Aktualizacja wdrożenia

Po zmianach w repozytorium:

```bash
cd ~/sportai-hub
git pull
docker compose up -d --build
```

## Testy wdrożeniowe

W ramach testów wdrożeniowych wykonano:

1. uruchomienie wszystkich kontenerów przez Docker Compose,
2. sprawdzenie stanu kontenerów przez `docker compose ps`,
3. sprawdzenie endpointu `/actuator/health`,
4. sprawdzenie endpointów REST API,
5. dodanie członka,
6. dodanie projektu,
7. dodanie zadania,
8. zmianę statusu zadania,
9. sprawdzenie historii aktywności,
10. sprawdzenie komunikacji WebSocket,
11. restart kontenerów,
12. sprawdzenie trwałości danych po restarcie.

Restart aplikacji:

```bash
docker compose restart
```

Po restarcie dane pozostają dostępne dzięki wykorzystaniu wolumenu Docker dla PostgreSQL.

## Podręcznik użytkownika

### Dashboard

Po otwarciu aplikacji użytkownik trafia na dashboard.

Widok prezentuje:

- liczbę członków,
- liczbę projektów,
- liczbę zadań,
- historię ostatnich aktywności,
- informacje odbierane w czasie rzeczywistym przez WebSocket.

### Dodawanie członka

1. Przejdź do zakładki `Członkowie`.
2. Wypełnij formularz.
3. Podaj imię, nazwisko, adres e-mail i specjalizację.
4. Zatwierdź formularz.
5. Nowy członek pojawi się na liście.

### Edycja członka

1. Przejdź do zakładki `Członkowie`.
2. Wybierz członka z listy.
3. Uruchom edycję.
4. Zmień wybrane dane.
5. Zapisz formularz.

### Usuwanie członka

1. Przejdź do zakładki `Członkowie`.
2. Wybierz opcję usunięcia.
3. Potwierdź operację.

Usunięcie może zostać zablokowane, jeżeli członek jest powiązany z projektem lub zadaniem.

### Dodawanie projektu

1. Przejdź do zakładki `Projekty`.
2. Wypełnij formularz projektu.
3. Podaj nazwę i opis.
4. Ustaw status.
5. Opcjonalnie wybierz lidera.
6. Zapisz projekt.

### Edycja projektu

1. Przejdź do zakładki `Projekty`.
2. Wybierz projekt.
3. Uruchom edycję.
4. Zmień dane.
5. Zapisz formularz.

### Usuwanie projektu

Projekt posiadający zadania nie może zostać usunięty bez wcześniejszego usunięcia powiązanych zadań.

### Dodawanie zadania

1. Przejdź do zakładki `Zadania`.
2. Wypełnij formularz zadania.
3. Podaj tytuł i opis.
4. Wybierz projekt.
5. Opcjonalnie przypisz członka.
6. Zapisz zadanie.

### Zmiana statusu zadania

Status zadania może zostać zmieniony bezpośrednio na liście zadań.

Przykładowe statusy:

```text
TODO
IN_PROGRESS
DONE
```

### Historia aktywności

Historia aktywności jest automatycznie uzupełniana po wykonaniu operacji biznesowych.

Przykładowe zdarzenia:

- utworzenie członka,
- aktualizacja projektu,
- dodanie zadania,
- zmiana statusu zadania,
- usunięcie obiektu.

## Dokumentacja projektu

Pełna dokumentacja znajduje się w katalogu:

```text
dokumentacja
```

Katalog zawiera:

- opis funkcjonalności,
- opis części serwerowej,
- opis części klienckiej,
- diagramy UML,
- dokumentację JavaDoc,
- opis testów,
- instrukcję uruchomieniową,
- instrukcję wdrożeniową,
- podstawowy podręcznik użytkownika,
- zrzuty ekranów aplikacji.

## Diagramy UML

Diagramy UML znajdują się w katalogu:

```text
dokumentacja\diagrams
```

Projekt zawiera:

- diagram przypadków użycia,
- diagram klas,
- diagram komponentów,
- diagram sekwencji,
- diagram wdrożenia.

## Autor

Paweł Lejczak