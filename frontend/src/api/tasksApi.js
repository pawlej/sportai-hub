import { httpClient } from "./httpClient";

export async function getTasks() {
    const response = await httpClient.get("/tasks");
    return response.data;
}

export async function createTask(task) {
    const response = await httpClient.post(
        "/tasks",
        task
    );

    return response.data;
}

export async function updateTask(id, task) {
    const response = await httpClient.put(
        `/tasks/${id}`,
        task
    );

    return response.data;
}

export async function updateTaskStatus(id, status) {
    const response = await httpClient.patch(
        `/tasks/${id}/status`,
        { status }
    );

    return response.data;
}

export async function deleteTask(id) {
    await httpClient.delete(`/tasks/${id}`);
}