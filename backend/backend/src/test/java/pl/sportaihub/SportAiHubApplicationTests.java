package pl.sportaihub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = SportAiHubApplication.class)
class SportAiHubApplicationTests {

    @Test
    void contextLoads() {
    }
}