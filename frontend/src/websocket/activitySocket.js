import { Client } from "@stomp/stompjs";

export function createActivityClient(
    onActivity,
    onConnectionChange
) {
    const protocol =
        window.location.protocol === "https:" ? "wss" : "ws";

    const brokerURL = import.meta.env.DEV
        ? `${protocol}://${window.location.hostname}:5173/ws`
        : `${protocol}://${window.location.host}/ws`;

    const client = new Client({
        brokerURL,
        reconnectDelay: 5000,
        heartbeatIncoming: 10000,
        heartbeatOutgoing: 10000,

        onConnect: () => {
            onConnectionChange?.(true);

            client.subscribe("/topic/activity", (message) => {
                onActivity(JSON.parse(message.body));
            });
        },

        onDisconnect: () => {
            onConnectionChange?.(false);
        },

        onWebSocketClose: () => {
            onConnectionChange?.(false);
        },

        onWebSocketError: (error) => {
            console.error("WebSocket error", error);
            onConnectionChange?.(false);
        },
    });

    return client;
}