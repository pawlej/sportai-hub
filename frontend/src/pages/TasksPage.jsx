import { useEffect, useState } from "react";
import { getMembers } from "../api/membersApi";
import { getProjects } from "../api/projectsApi";
import {
    createTask,
    deleteTask,
    getTasks,
    updateTask,
    updateTaskStatus,
} from "../api/tasksApi";

const emptyForm = {
    title: "",
    description: "",
    projectId: "",
    assignedMemberId: "",
    status: "TODO",
};

const statusLabels = {
    TODO: "Do zrobienia",
    IN_PROGRESS: "W realizacji",
    DONE: "Ukończone",
};

export default function TasksPage() {
    const [tasks, setTasks] = useState([]);
    const [projects, setProjects] = useState([]);
    const [members, setMembers] = useState([]);
    const [form, setForm] = useState(emptyForm);
    const [editingId, setEditingId] = useState(null);
    const [error, setError] = useState("");
    const [saving, setSaving] = useState(false);
    const [projectFilter, setProjectFilter] = useState("");

    async function loadData() {
        try {
            const [tasksData, projectsData, membersData] =
                await Promise.all([
                    getTasks(),
                    getProjects(),
                    getMembers(),
                ]);

            setTasks(tasksData);
            setProjects(projectsData);
            setMembers(membersData);
            setError("");
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    useEffect(() => {
        loadData();
    }, []);

    function handleChange(event) {
        const { name, value } = event.target;

        setForm((current) => ({
            ...current,
            [name]: value,
        }));
    }

    function startEditing(task) {
        setEditingId(task.id);

        setForm({
            title: task.title,
            description: task.description ?? "",
            status: task.status,
            projectId: task.projectId.toString(),
            assignedMemberId:
                task.assignedMemberId?.toString() ?? "",
        });

        window.scrollTo({
            top: 0,
            behavior: "smooth",
        });
    }

    function cancelEditing() {
        setEditingId(null);
        setForm(emptyForm);
        setError("");
    }

    async function handleSubmit(event) {
        event.preventDefault();
        setSaving(true);

        try {
            if (editingId === null) {
                await createTask({
                    title: form.title.trim(),
                    description: form.description.trim(),
                    projectId: Number(form.projectId),
                    assignedMemberId: form.assignedMemberId
                        ? Number(form.assignedMemberId)
                        : null,
                });
            } else {
                await updateTask(editingId, {
                    title: form.title.trim(),
                    description: form.description.trim(),
                    status: form.status,
                    projectId: Number(form.projectId),
                    assignedMemberId: form.assignedMemberId
                        ? Number(form.assignedMemberId)
                        : null,
                });
            }

            setForm(emptyForm);
            setEditingId(null);
            await loadData();
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setSaving(false);
        }
    }

    async function handleStatusChange(id, status) {
        try {
            await updateTaskStatus(id, status);
            await loadData();
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    async function handleDelete(id) {
        const accepted = window.confirm(
            "Czy na pewno chcesz usunąć to zadanie?"
        );

        if (!accepted) {
            return;
        }

        try {
            await deleteTask(id);

            if (editingId === id) {
                cancelEditing();
            }

            await loadData();
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    const visibleTasks = projectFilter
        ? tasks.filter(
            (task) =>
                task.projectId === Number(projectFilter)
        )
        : tasks;

    return (
        <section>
            <header className="page-header">
                <div>
                    <span className="eyebrow">Realizacja</span>

                    <h2>Zadania projektowe</h2>

                    <p>
                        Tworzenie zadań, przypisywanie członków i śledzenie
                        postępu realizacji.
                    </p>
                </div>
            </header>

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            <section className="panel">
                <div className="panel-header">
                    <div>
                        <h3>
                            {editingId === null
                                ? "Dodaj zadanie"
                                : "Edytuj zadanie"}
                        </h3>

                        <p>
                            Zadanie musi być przypisane do istniejącego projektu.
                        </p>
                    </div>

                    {editingId !== null && (
                        <button
                            type="button"
                            className="secondary-button"
                            onClick={cancelEditing}
                        >
                            Anuluj edycję
                        </button>
                    )}
                </div>

                {projects.length === 0 && (
                    <div className="warning-message">
                        Przed dodaniem zadania utwórz co najmniej jeden projekt.
                    </div>
                )}

                <form
                    className="form-grid"
                    onSubmit={handleSubmit}
                >
                    <label>
                        Tytuł zadania

                        <input
                            name="title"
                            value={form.title}
                            onChange={handleChange}
                            maxLength="200"
                            required
                        />
                    </label>

                    <label>
                        Projekt

                        <select
                            name="projectId"
                            value={form.projectId}
                            onChange={handleChange}
                            required
                        >
                            <option value="">
                                Wybierz projekt
                            </option>

                            {projects.map((project) => (
                                <option
                                    key={project.id}
                                    value={project.id}
                                >
                                    {project.name}
                                </option>
                            ))}
                        </select>
                    </label>

                    <label>
                        Przypisana osoba

                        <select
                            name="assignedMemberId"
                            value={form.assignedMemberId}
                            onChange={handleChange}
                        >
                            <option value="">
                                Nieprzypisane
                            </option>

                            {members.map((member) => (
                                <option
                                    key={member.id}
                                    value={member.id}
                                >
                                    {member.firstName} {member.lastName}
                                </option>
                            ))}
                        </select>
                    </label>

                    {editingId !== null && (
                        <label>
                            Status

                            <select
                                name="status"
                                value={form.status}
                                onChange={handleChange}
                            >
                                <option value="TODO">
                                    Do zrobienia
                                </option>

                                <option value="IN_PROGRESS">
                                    W realizacji
                                </option>

                                <option value="DONE">
                                    Ukończone
                                </option>
                            </select>
                        </label>
                    )}

                    <label className="full-width">
                        Opis

                        <textarea
                            name="description"
                            value={form.description}
                            onChange={handleChange}
                            rows="4"
                            maxLength="2000"
                        />
                    </label>

                    <button
                        className="primary-button"
                        disabled={saving || projects.length === 0}
                    >
                        {saving
                            ? "Zapisywanie..."
                            : editingId === null
                                ? "Dodaj zadanie"
                                : "Zapisz zmiany"}
                    </button>
                </form>
            </section>

            <section className="panel">
                <div className="panel-header">
                    <div>
                        <h3>Lista zadań</h3>

                        <p>
                            Możesz szybko zmienić status bez otwierania edycji.
                        </p>
                    </div>

                    <label className="filter-label">
                        Filtr projektu

                        <select
                            value={projectFilter}
                            onChange={(event) =>
                                setProjectFilter(event.target.value)
                            }
                        >
                            <option value="">
                                Wszystkie projekty
                            </option>

                            {projects.map((project) => (
                                <option
                                    key={project.id}
                                    value={project.id}
                                >
                                    {project.name}
                                </option>
                            ))}
                        </select>
                    </label>
                </div>

                <div className="cards-list">
                    {visibleTasks.map((task) => (
                        <article
                            className="task-card"
                            key={task.id}
                        >
                            <div className="task-content">
                                <div>
                  <span
                      className={`status ${task.status.toLowerCase()}`}
                  >
                    {statusLabels[task.status]}
                  </span>

                                    <h4>{task.title}</h4>

                                    <p>
                                        {task.description || "Brak opisu zadania"}
                                    </p>
                                </div>

                                <div className="task-metadata">
                  <span>
                    Projekt:{" "}
                      <strong>{task.projectName}</strong>
                  </span>

                                    <span>
                    Osoba:{" "}
                                        <strong>
                      {task.assignedMemberName || "nieprzypisana"}
                    </strong>
                  </span>
                                </div>
                            </div>

                            <div className="task-actions">
                                <select
                                    value={task.status}
                                    onChange={(event) =>
                                        handleStatusChange(
                                            task.id,
                                            event.target.value
                                        )
                                    }
                                >
                                    <option value="TODO">
                                        TODO
                                    </option>

                                    <option value="IN_PROGRESS">
                                        IN PROGRESS
                                    </option>

                                    <option value="DONE">
                                        DONE
                                    </option>
                                </select>

                                <button
                                    className="secondary-button"
                                    onClick={() => startEditing(task)}
                                >
                                    Edytuj
                                </button>

                                <button
                                    className="danger-button"
                                    onClick={() =>
                                        handleDelete(task.id)
                                    }
                                >
                                    Usuń
                                </button>
                            </div>
                        </article>
                    ))}

                    {visibleTasks.length === 0 && (
                        <div className="empty-state">
                            Brak zadań spełniających wybrane kryteria.
                        </div>
                    )}
                </div>
            </section>
        </section>
    );
}