package pl.sportaihub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sportaihub.entity.ActivityLog;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findTop20ByOrderByCreatedAtDesc();
}