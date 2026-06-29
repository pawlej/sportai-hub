package pl.sportaihub.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sportaihub.dto.ProjectRequest;
import pl.sportaihub.dto.ProjectResponse;
import pl.sportaihub.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectResponse> findAll() {
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    public ProjectResponse findById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(
            @Valid @RequestBody ProjectRequest request
    ) {
        return projectService.create(request);
    }

    @PutMapping("/{id}")
    public ProjectResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request
    ) {
        return projectService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }
}