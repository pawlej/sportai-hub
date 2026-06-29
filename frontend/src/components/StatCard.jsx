export default function StatCard({
                                     title,
                                     value,
                                     description,
                                 }) {
    return (
        <article className="stat-card">
            <span>{title}</span>
            <strong>{String(value).padStart(2, "0")}</strong>
            <small>{description}</small>
        </article>
    );
}