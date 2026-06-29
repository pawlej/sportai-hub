import { httpClient } from "./httpClient";

export async function getProjects() {
    const response = await httpClient.get("/projects");
    return response.data;
}

export async function createProject(project) {
    const response = await httpClient.post(
        "/projects",
        project
    );

    return response.data;
}

export async function updateProject(id, project) {
    const response = await httpClient.put(
        `/projects/${id}`,
        project
    );

    return response.data;
}

export async function deleteProject(id) {
    await httpClient.delete(`/projects/${id}`);
}