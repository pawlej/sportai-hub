import { useEffect, useState } from "react";
import { getMembers } from "../api/membersApi";
import {
    createProject,
    deleteProject,
    getProjects,
    updateProject,
} from "../api/projectsApi";

const emptyForm = {
    name: "",
    description: "",
    status: "PLANNED",
    leaderId: "",
};

const statusLabels = {
    PLANNED: "Planowany",
    ACTIVE: "Aktywny",
    DONE: "Zakończony",
};

export default function ProjectsPage() {
    const [projects, setProjects] = useState([]);
    const [members, setMembers] = useState([]);
    const [form, setForm] = useState(emptyForm);
    const [editingId, setEditingId] = useState(null);
    const [error, setError] = useState("");
    const [saving, setSaving] = useState(false);

    async function loadData() {
        try {
            const [projectsData, membersData] = await Promise.all([
                getProjects(),
                getMembers(),
            ]);

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

    function startEditing(project) {
        setEditingId(project.id);

        setForm({
            name: project.name,
            description: project.description ?? "",
            status: project.status,
            leaderId: project.leaderId?.toString() ?? "",
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

        const payload = {
            name: form.name.trim(),
            description: form.description.trim(),
            status: form.status,
            leaderId: form.leaderId
                ? Number(form.leaderId)
                : null,
        };

        try {
            if (editingId === null) {
                await createProject(payload);
            } else {
                await updateProject(editingId, payload);
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

    async function handleDelete(id) {
        const accepted = window.confirm(
            "Czy na pewno chcesz usunąć ten projekt?"
        );

        if (!accepted) {
            return;
        }

        try {
            await deleteProject(id);

            if (editingId === id) {
                cancelEditing();
            }

            await loadData();
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    return (
        <section>
            <header className="page-header">
                <div>
                    <span className="eyebrow">Portfolio</span>

                    <h2>Projekty</h2>

                    <p>
                        Zarządzanie projektami realizowanymi w kole naukowym.
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
                                ? "Dodaj projekt"
                                : "Edytuj projekt"}
                        </h3>

                        <p>
                            Projekt może mieć status, opis oraz przypisanego lidera.
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

                <form
                    className="form-grid"
                    onSubmit={handleSubmit}
                >
                    <label>
                        Nazwa projektu

                        <input
                            name="name"
                            value={form.name}
                            onChange={handleChange}
                            maxLength="150"
                            required
                        />
                    </label>

                    <label>
                        Status

                        <select
                            name="status"
                            value={form.status}
                            onChange={handleChange}
                            required
                        >
                            <option value="PLANNED">
                                Planowany
                            </option>

                            <option value="ACTIVE">
                                Aktywny
                            </option>

                            <option value="DONE">
                                Zakończony
                            </option>
                        </select>
                    </label>

                    <label>
                        Lider projektu

                        <select
                            name="leaderId"
                            value={form.leaderId}
                            onChange={handleChange}
                        >
                            <option value="">
                                Brak przypisanego lidera
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
                        disabled={saving}
                    >
                        {saving
                            ? "Zapisywanie..."
                            : editingId === null
                                ? "Dodaj projekt"
                                : "Zapisz zmiany"}
                    </button>
                </form>
            </section>

            <section className="panel">
                <h3>Lista projektów</h3>

                <div className="cards-list">
                    {projects.map((project) => (
                        <article
                            className="project-card"
                            key={project.id}
                        >
                            <div>
                <span
                    className={`status ${project.status.toLowerCase()}`}
                >
                  {statusLabels[project.status]}
                </span>

                                <h4>{project.name}</h4>

                                <p>
                                    {project.description || "Brak opisu projektu"}
                                </p>
                            </div>

                            <div className="project-footer">
                <span>
                  Lider:{" "}
                    <strong>
                    {project.leaderName || "nieprzypisany"}
                  </strong>
                </span>

                                <div className="action-buttons">
                                    <button
                                        className="secondary-button"
                                        onClick={() => startEditing(project)}
                                    >
                                        Edytuj
                                    </button>

                                    <button
                                        className="danger-button"
                                        onClick={() =>
                                            handleDelete(project.id)
                                        }
                                    >
                                        Usuń
                                    </button>
                                </div>
                            </div>
                        </article>
                    ))}

                    {projects.length === 0 && (
                        <div className="empty-state">
                            Brak projektów. Dodaj pierwszy projekt.
                        </div>
                    )}
                </div>
            </section>
        </section>
    );
}