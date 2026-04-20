import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./Pages/LoginPage";
import RegisterPage from "./Pages/RegisterPage";
import DashboardPage from "./Pages/DashboardPage";
import MissionBoardPage from "./Pages/MissionBoardPage";
import TaskDetailPage from "./Pages/TaskDetailPage";
import AdminPanel from "./Pages/adminPanel"; 
import Navbar from "./Components/Navbar";

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/mission-board" element={<MissionBoardPage />} />
        <Route path="/tasks/:id" element={<TaskDetailPage />} />
        <Route path="/admin" element={<AdminPanel />} /> 
      </Routes>
    </BrowserRouter>
  );
}

export default App;