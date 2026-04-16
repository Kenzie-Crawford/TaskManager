import { BrowserRouter, Routes, Route } from "react-router-dom";

import LoginPage from "./Pages/LoginPage";
import RegisterPage from "./Pages/RegisterPage";
import DashboardPage from "./Pages/DashboardPage";
import MissionBoard from "./Pages/MissionBoardPage";
import TaskDetailPage from "./Pages/TaskDetailPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/missions" element={<MissionBoard />} />
        <Route path="/tasks" element={<TaskDetailPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;