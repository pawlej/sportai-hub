package pl.sportaihub.entity;

import jakarta.persistence.*;
import pl.sportaihub.enums.ActivityType;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ActivityType type;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ActivityLog() {
    }

    public ActivityLog(ActivityType type, String message) {
        this.type = type;
        this.message = message;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}