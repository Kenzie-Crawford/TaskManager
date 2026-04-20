import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../Services/api";

function AdminPanel() {
  const [users, setUsers] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [showTaskForm, setShowTaskForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [newTask, setNewTask] = useState({
    title: "",
    description: "",
    priority: "MEDIUM",
    points: 10,
    dueDate: "",
    assignedToId: null,
  });

  const navigate = useNavigate();

  useEffect(() => {
    checkAdminAccess();
    fetchData();
  }, []);

  const checkAdminAccess = () => {
    const userRole = localStorage.getItem("userRole");
    if (userRole !== "MANAGER" && userRole !== "ADMIN") {
      navigate("/dashboard");
    }
  };

  const fetchData = async () => {
    try {
      const [usersRes, tasksRes] = await Promise.all([
        API.get("/users"),
        API.get("/tasks"),
      ]);
      setUsers(usersRes.data);
      setTasks(tasksRes.data);
    } catch (err) {
      console.error("Failed to load data", err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (e) => {
    e.preventDefault();
    try {
      const createdById = localStorage.getItem("userId");
      await API.post("/tasks", { ...newTask, createdById });
      setShowTaskForm(false);
      setNewTask({
        title: "",
        description: "",
        priority: "MEDIUM",
        points: 10,
        dueDate: "",
        assignedToId: null,
      });
      fetchData(); // Refresh task list
    } catch (err) {
      console.error("Failed to create task", err);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1>Admin Panel</h1>
      
      <button onClick={() => setShowTaskForm(!showTaskForm)}>
        {showTaskForm ? "Cancel" : "+ Create New Task"}
      </button>

      {showTaskForm && (
        <form onSubmit={handleCreateTask}>
          <h2>Create New Task</h2>
          
          <input
            type="text"
            placeholder="Task Title"
            value={newTask.title}
            onChange={(e) => setNewTask({...newTask, title: e.target.value})}
            required
          />
          
          <textarea
            placeholder="Description"
            value={newTask.description}
            onChange={(e) => setNewTask({...newTask, description: e.target.value})}
          />
          
          <select
            value={newTask.priority}
            onChange={(e) => setNewTask({...newTask, priority: e.target.value})}
          >
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
            <option value="URGENT">Urgent</option>
          </select>
          
          <input
            type="number"
            placeholder="Points"
            value={newTask.points}
            onChange={(e) => setNewTask({...newTask, points: parseInt(e.target.value)})}
          />
          
          <input
            type="date"
            value={newTask.dueDate}
            onChange={(e) => setNewTask({...newTask, dueDate: e.target.value})}
          />
          
          <select
            value={newTask.assignedToId || ""}
            onChange={(e) => setNewTask({...newTask, assignedToId: e.target.value ? parseInt(e.target.value) : null})}
          >
            <option value="">Unassigned (Mission Board)</option>
            {users.map(user => (
              <option key={user.id} value={user.id}>{user.name}</option>
            ))}
          </select>
          
          <button type="submit">Create Task</button>
        </form>
      )}

      <h2>All Tasks</h2>
      {tasks.map(task => (
        <div key={task.id}>
          <h3>{task.title}</h3>
          <p>Status: {task.status}</p>
          <p>Assigned to: {task.assignedTo?.username || "Unassigned"}</p>
        </div>
      ))}
    </div>
  );
}

export default AdminPanel;