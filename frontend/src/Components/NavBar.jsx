import { Link, useNavigate } from "react-router-dom";

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

  if(!isAuthenticated) {
    return null; 
  }
  
  return (
    <nav>
      <h2>Task Manager</h2>

      <div>
        <Link to="/dashboard">Dashboard</Link>
        {" | "}
        <Link to="/mission-board">Mission Board</Link>
        
        {(userRole === "MANAGER" || userRole === "ADMIN") && (
          <>
            {" | "}
            <Link to="/admin">Admin Panel</Link>
          </>
        )}
        
        {" | "}
        <button onClick={handleLogout}>Logout</button>
      </div>
    </nav>
  );
}

export default Navbar;
