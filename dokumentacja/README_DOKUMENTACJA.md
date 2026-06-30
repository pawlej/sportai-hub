# Dokumentacja projektu SportAI Hub Mini

## 1. Cel projektu

Celem projektu było opracowanie aplikacji klient-serwer wspierającej zarządzanie działalnością koła naukowego zajmującego się zastosowaniem sztucznej inteligencji w sporcie.

System umożliwia zarządzanie członkami, projektami oraz zadaniami realizowanymi przez zespoły projektowe. Udostępnia również dashboard prezentujący podstawowe statystyki i historię aktywności.

## 2. Architektura systemu

Aplikacja została wykonana w architekturze klient-serwer.

Część kliencka została przygotowana jako aplikacja SPA w technologii React. Komunikacja z serwerem odbywa się przez REST API oraz WebSocket.

Część serwerowa została wykonana w technologii Spring Boot. Dostęp do danych został zrealizowany przy użyciu Spring Data JPA.

Dane przechowywane są w relacyjnej bazie PostgreSQL.

## 3. Część serwerowa

Backend aplikacji udostępnia operacje CRUD dla następujących zasobów:

- członkowie,
- projekty,
- zadania,
- historia aktywności.

Warstwa serwerowa została podzielona na:

- kontrolery REST,
- serwisy,
- repozytoria,
- encje JPA,
- obiekty DTO,
- konfigurację,
- obsługę wyjątków,
- aspekt rejestrujący zdarzenia.

## 4. Spring Data JPA

Dostęp do danych realizowany jest przez interfejsy repozytoriów rozszerzające Spring Data JPA.

Encje aplikacji są odwzorowywane na tabele PostgreSQL. Relacje obejmują przypisanie lidera do projektu, członka do zadania oraz zadania do projektu.

## 5. Spring AOP

Aplikacja wykorzystuje Spring AOP do automatycznego rejestrowania zdarzeń biznesowych.

Metody serwisowe oznaczone adnotacją LogActivity są przechwytywane przez ActivityLogAspect. Aspekt zapisuje informację o zdarzeniu w tabeli historii aktywności oraz publikuje zdarzenie przez WebSocket.

## 6. WebSocket

Komunikacja czasu rzeczywistego wykorzystuje WebSocket i protokół STOMP.

Klient łączy się przez endpoint:

/ws

i subskrybuje kanał:

/topic/activity

Dzięki temu nowe zdarzenia pojawiają się na dashboardzie bez konieczności ręcznego odświeżania strony.

## 7. Część kliencka

Frontend został wykonany w technologii React i Vite.

Aplikacja posiada następujące widoki:

- dashboard,
- członkowie,
- projekty,
- zadania.

Klient komunikuje się z REST API przy użyciu biblioteki Axios. Połączenie WebSocket jest obsługiwane przez bibliotekę STOMP.

## 8. Konteneryzacja

Aplikacja uruchamiana jest przez Docker Compose.

W skład systemu wchodzą trzy kontenery:

- frontend React uruchamiany przez Nginx,
- backend Spring Boot,
- baza PostgreSQL.

Frontend pełni również funkcję reverse proxy dla REST API i WebSocket.

## 9. Wdrożenie

Aplikacja została wdrożona na maszynie wirtualnej o adresie:

172.20.41.28

Aplikacja działa na porcie:

8085

Adres aplikacji:

http://172.20.41.28:8085

Dostęp jest możliwy z sieci uczelnianej lub przez tunel SSH przez serwer Taurus.

## 10. Testy

W części serwerowej wykorzystano JUnit, Spring Boot Test, MockMvc oraz bazę H2.

Przeprowadzono testy obejmujące:

- uruchomienie kontekstu Spring,
- tworzenie członka,
- odrzucenie zduplikowanego adresu e-mail,
- tworzenie projektu i zadania,
- zmianę statusu zadania,
- blokadę usunięcia projektu posiadającego zadania,
- zapis historii aktywności przez AOP.

## 11. Testy wdrożeniowe

Wykonano następujące testy wdrożeniowe:

1. Sprawdzenie działania kontenerów poleceniem docker compose ps.
2. Sprawdzenie endpointu /actuator/health.
3. Sprawdzenie endpointów REST API.
4. Dodanie członka, projektu i zadania.
5. Zmiana statusu zadania.
6. Sprawdzenie komunikacji WebSocket.
7. Restart kontenerów.
8. Sprawdzenie trwałości danych po restarcie.

## 12. Instrukcja uruchomienia

Należy skopiować plik .env.example jako .env i ustawić hasło PostgreSQL.

Następnie należy wykonać:

docker compose up -d --build

Aplikacja będzie dostępna pod adresem:

http://localhost:8085

Zatrzymanie aplikacji:

docker compose down

## 13. Podręcznik użytkownika

### Dodawanie członka

Należy przejść do zakładki Członkowie, wypełnić formularz i zatwierdzić operację.

### Dodawanie projektu

Należy przejść do zakładki Projekty, podać nazwę, opis, status oraz opcjonalnie wybrać lidera.

### Dodawanie zadania

Należy przejść do zakładki Zadania, podać tytuł, wybrać projekt i opcjonalnie przypisać członka.

### Zmiana statusu zadania

Status zadania można zmienić bezpośrednio na liście zadań.

### Dashboard

Dashboard przedstawia liczbę członków, projektów i zadań oraz historię ostatnich aktywności.