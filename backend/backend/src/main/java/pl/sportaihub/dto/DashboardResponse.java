package pl.sportaihub.dto;

public record DashboardResponse(
        long membersCount,
        long activeMembersCount,
        long projectsCount,
        long activeProjectsCount,
        long tasksTodo,
        long tasksInProgress,
        long tasksDone
) {
}