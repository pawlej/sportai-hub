package pl.sportaihub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.sportaihub.repository.ActivityLogRepository;
import pl.sportaihub.repository.MemberRepository;
import pl.sportaihub.repository.ProjectRepository;
import pl.sportaihub.repository.ProjectTaskRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = SportAiHubApplication.class)
@AutoConfigureMockMvc
class SportAiHubIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void cleanDatabase() {
        activityLogRepository.deleteAll();
        projectTaskRepository.deleteAll();
        projectRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void shouldCreateAndReturnMember() throws Exception {
        String memberJson = """
            {
              "firstName": "Jan",
              "lastName": "Kowalski",
              "email": "jan.kowalski@example.com",
              "specialization": "MACHINE_LEARNING",
              "active": true
            }
            """;

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.email").value("jan.kowalski@example.com"))
                .andExpect(jsonPath("$.specialization").value("MACHINE_LEARNING"))
                .andExpect(jsonPath("$.active").value(true));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email")
                        .value("jan.kowalski@example.com"));
    }

    @Test
    void shouldRejectDuplicateMemberEmail() throws Exception {
        String memberJson = """
            {
              "firstName": "Jan",
              "lastName": "Kowalski",
              "email": "duplicate@example.com",
              "specialization": "DATA_ENGINEERING",
              "active": true
            }
            """;

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateProjectAndTaskAndChangeTaskStatus() throws Exception {
        Long memberId = createMember();
        Long projectId = createProject(memberId);

        String taskJson = """
            {
              "title": "Przygotowanie datasetu",
              "description": "Oczyszczenie i walidacja danych wejściowych",
              "projectId": %d,
              "assignedMemberId": %d
            }
            """.formatted(projectId, memberId);

        String taskResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Przygotowanie datasetu"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode taskNode = objectMapper.readTree(taskResponse);
        long taskId = taskNode.get("id").asLong();

        String statusJson = """
            {
              "status": "IN_PROGRESS"
            }
            """;

        mockMvc.perform(patch("/api/tasks/{id}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));
    }

    @Test
    void shouldPreventDeletingProjectWithTasks() throws Exception {
        Long memberId = createMember();
        Long projectId = createProject(memberId);

        String taskJson = """
            {
              "title": "Implementacja modelu",
              "description": "Trening modelu klasyfikacyjnego",
              "projectId": %d,
              "assignedMemberId": %d
            }
            """.formatted(projectId, memberId);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/projects/{id}", projectId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateActivityLogThroughAop() throws Exception {
        createMember();

        assertThat(activityLogRepository.count()).isGreaterThan(0);
    }

    private Long createMember() throws Exception {
        String memberJson = """
    {
      "firstName": "Jan",
      "lastName": "Kowalski",
      "email": "duplicate@example.com",
      "specialization": "DATA_ENGINEERING",
      "active": true
    }
    """;

        String response = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private Long createProject(Long memberId) throws Exception {
        String projectJson = """
            {
              "name": "Analiza obrazu sportowego",
              "description": "Projekt Computer Vision dla materiałów meczowych",
              "status": "ACTIVE",
              "leaderId": %d
            }
            """.formatted(memberId);

        String response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }
}