import { httpClient } from "./httpClient";

export async function getMembers() {
    const response = await httpClient.get("/members");
    return response.data;
}

export async function createMember(member) {
    const response = await httpClient.post("/members", member);
    return response.data;
}

export async function updateMember(id, member) {
    const response = await httpClient.put(
        `/members/${id}`,
        member
    );

    return response.data;
}

export async function deleteMember(id) {
    await httpClient.delete(`/members/${id}`);
}