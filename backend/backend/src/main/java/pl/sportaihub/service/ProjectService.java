package pl.sportaihub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sportaihub.aspect.LogActivity;
import pl.sportaihub.dto.ProjectRequest;
import pl.sportaihub.dto.ProjectResponse;
import pl.sportaihub.entity.Member;
import pl.sportaihub.entity.Project;
import pl.sportaihub.enums.ActivityType;
import pl.sportaihub.exception.BadRequestException;
import pl.sportaihub.exception.NotFoundException;
import pl.sportaihub.repository.ProjectRepository;
import pl.sportaihub.repository.ProjectTaskRepository;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository taskRepository;
    private final MemberService memberService;

    public ProjectService(
            ProjectRepository projectRepository,
            ProjectTaskRepository taskRepository,
            MemberService memberService
    ) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(Long id) {
        return toResponse(getProject(id));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.PROJECT_CREATED,
            message = "Utworzono projekt"
    )
    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project();
        updateEntity(project, request);

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.PROJECT_UPDATED,
            message = "Zaktualizowano projekt"
    )
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = getProject(id);
        updateEntity(project, request);

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.PROJECT_DELETED,
            message = "Usunięto projekt"
    )
    public void delete(Long id) {
        Project project = getProject(id);

        if (taskRepository.existsByProjectId(id)) {
            throw new BadRequestException(
                    "Cannot delete project because it still contains tasks"
            );
        }

        projectRepository.delete(project);
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Project not found: " + id)
                );
    }

    private void updateEntity(Project project, ProjectRequest request) {
        project.setName(request.name().trim());
        project.setDescription(request.description());
        project.setStatus(request.status());

        Member leader = request.leaderId() == null
                ? null
                : memberService.getMember(request.leaderId());

        project.setLeader(leader);
    }

    private ProjectResponse toResponse(Project project) {
        Member leader = project.getLeader();

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                leader == null ? null : leader.getId(),
                leader == null
                        ? null
                        : leader.getFirstName() + " " + leader.getLastName(),
                project.getCreatedAt()
        );
    }
}