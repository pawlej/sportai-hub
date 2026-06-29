import { NavLink } from "react-router-dom";

export default function Navbar() {
    return (
        <aside className="sidebar">
            <div className="brand">
                <div className="brand-logo">AI</div>

                <div>
                    <h1>SportAI Hub</h1>
                    <p>Panel zarządu</p>
                </div>
            </div>

            <nav className="navigation">
                <NavLink to="/" end>
                    Dashboard
                </NavLink>

                <NavLink to="/members">
                    Członkowie
                </NavLink>

                <NavLink to="/projects">
                    Projekty
                </NavLink>

                <NavLink to="/tasks">
                    Zadania
                </NavLink>
            </nav>
        </aside>
    );
}