import {
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import Layout from "./components/Layout";
import DashboardPage from "./pages/DashboardPage";
import MembersPage from "./pages/MembersPage";
import ProjectsPage from "./pages/ProjectsPage";
import TasksPage from "./pages/TasksPage";

export default function App() {
  return (
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<DashboardPage />} />

          <Route
              path="/members"
              element={<MembersPage />}
          />

          <Route
              path="/projects"
              element={<ProjectsPage />}
          />

          <Route
              path="/tasks"
              element={<TasksPage />}
          />

          <Route
              path="*"
              element={<Navigate to="/" replace />}
          />
        </Route>
      </Routes>
  );
}