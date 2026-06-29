package pl.sportaihub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sportaihub.dto.DashboardResponse;
import pl.sportaihub.enums.ProjectStatus;
import pl.sportaihub.enums.TaskStatus;
import pl.sportaihub.repository.MemberRepository;
import pl.sportaihub.repository.ProjectRepository;
import pl.sportaihub.repository.ProjectTaskRepository;

@Service
public class DashboardService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository taskRepository;

    public DashboardService(
            MemberRepository memberRepository,
            ProjectRepository projectRepository,
            ProjectTaskRepository taskRepository
    ) {
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        return new DashboardResponse(
                memberRepository.count(),
                memberRepository.countByActiveTrue(),
                projectRepository.count(),
                projectRepository.countByStatus(ProjectStatus.ACTIVE),
                taskRepository.countByStatus(TaskStatus.TODO),
                taskRepository.countByStatus(TaskStatus.IN_PROGRESS),
                taskRepository.countByStatus(TaskStatus.DONE)
        );
    }
}