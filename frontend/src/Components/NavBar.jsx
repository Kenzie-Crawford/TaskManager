import { Link, useNavigate } from "react-router-dom";
import logo from "../assets/workflownobg.png";

function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const userRole = localStorage.getItem("userRole");
  const isAuthenticated = !!token;

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("userRole");
    navigate("/");
  };

  if (!isAuthenticated) {
    return null;
  }

  return (
    <nav>
      <div style={{ display: "flex", alignItems: "center", gap: "5px" }}>
        <img src={logo} alt="WorkFlow logo" style={{ height: "50px", width: "50px" }} />
        <span className="nav-brand">Work Flow</span>
      </div>
      <div>
        <Link to="/dashboard">Dashboard</Link>
        {" | "}
        <Link to="/mission-board">Mission Board</Link>
        {" | "}
        <Link to="/achievements">Achievements</Link>
        {" | "}
        <Link to="/leaderboard">Leaderboard</Link>

        {(userRole === "MANAGER" || userRole === "ADMIN") && (
          <>
            {" | "}
            <Link to="/admin">Admin Panel</Link>
            {" | "}
            <Link to="/create-task">Create Task</Link>
          </>
        )}

        {" | "}
        <button onClick={handleLogout}>Logout</button>
      </div>
    </nav>
  );
}

export default Navbar;