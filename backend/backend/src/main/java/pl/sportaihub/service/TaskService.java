package pl.sportaihub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sportaihub.aspect.LogActivity;
import pl.sportaihub.dto.*;
import pl.sportaihub.entity.Member;
import pl.sportaihub.entity.Project;
import pl.sportaihub.entity.ProjectTask;
import pl.sportaihub.enums.ActivityType;
import pl.sportaihub.enums.TaskStatus;
import pl.sportaihub.exception.NotFoundException;
import pl.sportaihub.repository.ProjectTaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final ProjectTaskRepository taskRepository;
    private final ProjectService projectService;
    private final MemberService memberService;

    public TaskService(
            ProjectTaskRepository taskRepository,
            ProjectService projectService,
            MemberService memberService
    ) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findByProject(Long projectId) {
        projectService.getProject(projectId);

        return taskRepository.findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse findById(Long id) {
        return toResponse(getTask(id));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.TASK_CREATED,
            message = "Utworzono zadanie"
    )
    public TaskResponse create(TaskRequest request) {
        ProjectTask task = new ProjectTask();

        task.setTitle(request.title().trim());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.TODO);
        task.setProject(projectService.getProject(request.projectId()));
        task.setAssignedMember(resolveMember(request.assignedMemberId()));

        return toResponse(taskRepository.save(task));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.TASK_UPDATED,
            message = "Zaktualizowano zadanie"
    )
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        ProjectTask task = getTask(id);

        task.setTitle(request.title().trim());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setProject(projectService.getProject(request.projectId()));
        task.setAssignedMember(resolveMember(request.assignedMemberId()));

        return toResponse(taskRepository.save(task));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.TASK_STATUS_CHANGED,
            message = "Zmieniono status zadania"
    )
    public TaskResponse updateStatus(Long id, TaskStatusRequest request) {
        ProjectTask task = getTask(id);
        task.setStatus(request.status());

        return toResponse(taskRepository.save(task));
    }

    @Transactional
    @LogActivity(
            type = ActivityType.TASK_DELETED,
            message = "Usunięto zadanie"
    )
    public void delete(Long id) {
        taskRepository.delete(getTask(id));
    }

    private ProjectTask getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Task not found: " + id)
                );
    }

    private Member resolveMember(Long memberId) {
        return memberId == null ? null : memberService.getMember(memberId);
    }

    private TaskResponse toResponse(ProjectTask task) {
        Project project = task.getProject();
        Member member = task.getAssignedMember();

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                project.getId(),
                project.getName(),
                member == null ? null : member.getId(),
                member == null
                        ? null
                        : member.getFirstName() + " " + member.getLastName(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}