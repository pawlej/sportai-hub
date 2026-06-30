package pl.sportaihub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główny punkt uruchomieniowy aplikacji serwerowej SportAI Hub.
 *
 * <p>Aplikacja wykorzystuje Spring Boot, Spring Data JPA,
 * Spring AOP, WebSocket oraz bazę danych PostgreSQL.</p>
 */
@SpringBootApplication
public class SportAiHubApplication {

    /**
     * Uruchamia aplikację Spring Boot.
     *
     * @param args argumenty przekazane podczas uruchomienia
     */
    public static void main(String[] args) {
        SpringApplication.run(SportAiHubApplication.class, args);
    }
}