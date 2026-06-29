import { useEffect, useState } from "react";

import {
    createMember,
    deleteMember,
    getMembers,
} from "../api/membersApi";

const emptyForm = {
    firstName: "",
    lastName: "",
    email: "",
    specialization: "MACHINE_LEARNING",
    active: true,
};

const specializationLabels = {
    FOOTBALL_ANALYTICS: "Football Analytics",
    COMPUTER_VISION: "Computer Vision",
    DATA_ENGINEERING: "Data Engineering",
    MACHINE_LEARNING: "Machine Learning",
    BIOMECHANICS: "Biomechanika",
    OTHER: "Inne",
};

export default function MembersPage() {
    const [members, setMembers] =
        useState([]);

    const [form, setForm] =
        useState(emptyForm);

    const [error, setError] =
        useState("");

    const [saving, setSaving] =
        useState(false);

    async function loadMembers() {
        try {
            setMembers(await getMembers());
            setError("");
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    useEffect(() => {
        loadMembers();
    }, []);

    function handleChange(event) {
        const {
            name,
            value,
            type,
            checked,
        } = event.target;

        setForm((current) => ({
            ...current,
            [name]:
                type === "checkbox"
                    ? checked
                    : value,
        }));
    }

    async function handleSubmit(event) {
        event.preventDefault();
        setSaving(true);

        try {
            await createMember(form);
            setForm(emptyForm);
            await loadMembers();
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setSaving(false);
        }
    }

    async function handleDelete(id) {
        if (
            !window.confirm(
                "Czy na pewno usunąć tego członka?"
            )
        ) {
            return;
        }

        try {
            await deleteMember(id);
            await loadMembers();
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    return (
        <section>
            <header className="page-header">
                <div>
          <span className="eyebrow">
            Zespół
          </span>

                    <h2>Członkowie koła</h2>

                    <p>
                        Zarządzanie osobami zaangażowanymi
                        w działalność koła.
                    </p>
                </div>
            </header>

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            <section className="panel">
                <h3>Dodaj członka</h3>

                <form
                    className="form-grid"
                    onSubmit={handleSubmit}
                >
                    <label>
                        Imię

                        <input
                            name="firstName"
                            value={form.firstName}
                            onChange={handleChange}
                            required
                        />
                    </label>

                    <label>
                        Nazwisko

                        <input
                            name="lastName"
                            value={form.lastName}
                            onChange={handleChange}
                            required
                        />
                    </label>

                    <label>
                        E-mail

                        <input
                            type="email"
                            name="email"
                            value={form.email}
                            onChange={handleChange}
                            required
                        />
                    </label>

                    <label>
                        Specjalizacja

                        <select
                            name="specialization"
                            value={form.specialization}
                            onChange={handleChange}
                        >
                            {Object.entries(
                                specializationLabels
                            ).map(([value, label]) => (
                                <option
                                    key={value}
                                    value={value}
                                >
                                    {label}
                                </option>
                            ))}
                        </select>
                    </label>

                    <label className="checkbox-label">
                        <input
                            type="checkbox"
                            name="active"
                            checked={form.active}
                            onChange={handleChange}
                        />

                        Aktywny
                    </label>

                    <button
                        className="primary-button"
                        disabled={saving}
                    >
                        {saving
                            ? "Zapisywanie..."
                            : "Dodaj członka"}
                    </button>
                </form>
            </section>

            <section className="panel">
                <h3>Lista członków</h3>

                <div className="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Imię i nazwisko</th>
                            <th>E-mail</th>
                            <th>Specjalizacja</th>
                            <th>Status</th>
                            <th>Akcja</th>
                        </tr>
                        </thead>

                        <tbody>
                        {members.map((member) => (
                            <tr key={member.id}>
                                <td>
                                    {member.firstName}{" "}
                                    {member.lastName}
                                </td>

                                <td>{member.email}</td>

                                <td>
                                    {
                                        specializationLabels[
                                            member.specialization
                                            ]
                                    }
                                </td>

                                <td>
                    <span
                        className={
                            member.active
                                ? "status active"
                                : "status inactive"
                        }
                    >
                      {member.active
                          ? "Aktywny"
                          : "Nieaktywny"}
                    </span>
                                </td>

                                <td>
                                    <button
                                        className="danger-button"
                                        onClick={() =>
                                            handleDelete(member.id)
                                        }
                                    >
                                        Usuń
                                    </button>
                                </td>
                            </tr>
                        ))}

                        {members.length === 0 && (
                            <tr>
                                <td
                                    colSpan="5"
                                    className="empty-state"
                                >
                                    Brak członków.
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </section>
        </section>
    );
}