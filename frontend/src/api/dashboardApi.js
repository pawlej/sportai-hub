import { httpClient } from "./httpClient";

export async function getDashboard() {
    const response = await httpClient.get("/dashboard");
    return response.data;
}

export async function getActivities() {
    const response = await httpClient.get("/activities");
    return response.data;
}