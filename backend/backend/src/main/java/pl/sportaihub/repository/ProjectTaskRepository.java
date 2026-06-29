package pl.sportaihub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sportaihub.entity.ProjectTask;
import pl.sportaihub.enums.TaskStatus;

import java.util.List;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {

    long countByStatus(TaskStatus status);

    List<ProjectTask> findByProjectIdOrderByCreatedAtDesc(Long projectId);

    boolean existsByProjectId(Long projectId);

    boolean existsByAssignedMemberId(Long memberId);
}