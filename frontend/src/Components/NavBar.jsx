import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

function Navbar() {
  const navigate = useNavigate();
  const [userRole, setUserRole] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  
  useEffect(() => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("userRole");
    
    // Use alert for debugging
    alert(`Token: ${!!token}, Role: ${role}`);
    
    setUserRole(role);
    setIsAuthenticated(!!token);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("userRole");
    setIsAuthenticated(false);
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