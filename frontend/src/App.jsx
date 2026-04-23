import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./Pages/LoginPage";
import RegisterPage from "./Pages/RegisterPage";
import DashboardPage from "./Pages/DashboardPage";
import MissionBoardPage from "./Pages/MissionBoardPage";
import TaskDetailPage from "./Pages/TaskDetailPage";
import AdminPanel from "./Pages/adminPanel";
import AchievementsPage from "./Pages/AchievementsPage";
import LeaderBoardPage from "./Pages/LeaderBoardPage";
import NavBar from "./Components/NavBar";
import CreateTaskPage from "./Pages/CreateTaskPage";
import HeroPage from "./Pages/HeroPage";
import Particles from "./Components/Particles";

function App() {
  return (
    <BrowserRouter>
    <Particles/>
      <NavBar />
      <Routes>
        <Route path= "/" element = {<HeroPage/>}/>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/mission-board" element={<MissionBoardPage />} />
        <Route path="/tasks/:id" element={<TaskDetailPage />} />
        <Route path="/admin" element={<AdminPanel />} />
        <Route path="/achievements" element={<AchievementsPage />} />
        <Route path="/leaderboard" element={<LeaderBoardPage />} />
        <Route path="/create-task" element={<CreateTaskPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;