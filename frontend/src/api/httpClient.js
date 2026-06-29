import axios from "axios";

export const httpClient = axios.create({
    baseURL: "/api",

    headers: {
        "Content-Type": "application/json",
    },
});

httpClient.interceptors.response.use(
    (response) => response,

    (error) => {
        const backendMessage = error.response?.data?.message;

        const validationErrors =
            error.response?.data?.validationErrors;

        let message =
            backendMessage ??
            error.message ??
            "Wystąpił nieznany błąd";

        if (
            validationErrors &&
            Object.keys(validationErrors).length > 0
        ) {
            message = Object.values(validationErrors).join(", ");
        }

        return Promise.reject(new Error(message));
    }
);