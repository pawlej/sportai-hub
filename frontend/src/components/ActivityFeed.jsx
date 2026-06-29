function formatDate(value) {
    if (!value) {
        return "";
    }

    return new Intl.DateTimeFormat("pl-PL", {
        dateStyle: "short",
        timeStyle: "medium",
    }).format(new Date(value));
}

export default function ActivityFeed({
                                         activities,
                                         connected,
                                     }) {
    return (
        <section className="panel">
            <div className="panel-header">
                <div>
                    <h3>Aktywność systemu</h3>

                    <p>
                        Zdarzenia zapisywane przez AOP
                        i przesyłane przez WebSocket.
                    </p>
                </div>

                <span
                    className={
                        connected
                            ? "connection connected"
                            : "connection disconnected"
                    }
                >
          {connected ? "LIVE" : "OFFLINE"}
        </span>
            </div>

            {activities.length === 0 ? (
                <div className="empty-state">
                    Brak aktywności.
                </div>
            ) : (
                <div className="activity-list">
                    {activities.map((activity) => (
                        <article
                            className="activity-item"
                            key={activity.id}
                        >
                            <div>
                                <strong>{activity.type}</strong>
                                <p>{activity.message}</p>
                            </div>

                            <time>
                                {formatDate(activity.createdAt)}
                            </time>
                        </article>
                    ))}
                </div>
            )}
        </section>
    );
}