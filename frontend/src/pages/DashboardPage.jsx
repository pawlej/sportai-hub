import { useEffect, useState } from "react";
import {
    getActivities,
    getDashboard,
} from "../api/dashboardApi";

import ActivityFeed from "../components/ActivityFeed";
import StatCard from "../components/StatCard";
import { createActivityClient } from "../websocket/activitySocket";

const emptyDashboard = {
    membersCount: 0,
    activeMembersCount: 0,
    projectsCount: 0,
    activeProjectsCount: 0,
    tasksTodo: 0,
    tasksInProgress: 0,
    tasksDone: 0,
};

export default function DashboardPage() {
    const [dashboard, setDashboard] =
        useState(emptyDashboard);

    const [activities, setActivities] =
        useState([]);

    const [connected, setConnected] =
        useState(false);

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState("");

    async function loadData() {
        try {
            const [
                dashboardResponse,
                activitiesResponse,
            ] = await Promise.all([
                getDashboard(),
                getActivities(),
            ]);

            setDashboard(dashboardResponse);
            setActivities(activitiesResponse);
            setError("");
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadData();

        const client = createActivityClient(
            (activity) => {
                setActivities((current) => [
                    activity,
                    ...current.filter(
                        (item) => item.id !== activity.id
                    ),
                ].slice(0, 20));

                getDashboard()
                    .then(setDashboard)
                    .catch(console.error);
            },

            setConnected
        );

        client.activate();

        return () => {
            client.deactivate();
        };
    }, []);

    return (
        <section>
            <header className="page-header">
                <div>
          <span className="eyebrow">
            SportAI Hub Mini
          </span>

                    <h2>Dashboard zarządu</h2>

                    <p>
                        Podsumowanie działalności koła
                        i realizowanych projektów.
                    </p>
                </div>

                <button
                    className="secondary-button"
                    onClick={loadData}
                >
                    Odśwież
                </button>
            </header>

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            {loading ? (
                <div className="panel">
                    Ładowanie danych...
                </div>
            ) : (
                <>
                    <div className="stats-grid">
                        <StatCard
                            title="Członkowie"
                            value={dashboard.membersCount}
                            description={`Aktywni: ${dashboard.activeMembersCount}`}
                        />

                        <StatCard
                            title="Projekty"
                            value={dashboard.projectsCount}
                            description={`Aktywne: ${dashboard.activeProjectsCount}`}
                        />

                        <StatCard
                            title="TODO"
                            value={dashboard.tasksTodo}
                            description="Zadania do wykonania"
                        />

                        <StatCard
                            title="W realizacji"
                            value={dashboard.tasksInProgress}
                            description="Zadania aktywne"
                        />

                        <StatCard
                            title="Ukończone"
                            value={dashboard.tasksDone}
                            description="Zadania zakończone"
                        />
                    </div>

                    <ActivityFeed
                        activities={activities}
                        connected={connected}
                    />
                </>
            )}
        </section>
    );
}