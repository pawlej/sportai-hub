package pl.sportaihub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sportaihub.entity.Project;
import pl.sportaihub.enums.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    long countByStatus(ProjectStatus status);

    boolean existsByLeaderId(Long leaderId);}