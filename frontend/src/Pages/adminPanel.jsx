import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../Services/api";
import LoadingSpinner from "../Components/LoadingSpinner";

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
      fetchData();
    } catch (err) {
      console.error("Failed to create task", err);
    }
  };

  if (loading) return <LoadingSpinner message="Loading admin panel..." />;

  return (
    <div className="admin-container">
      <h1>Admin Panel</h1>

      <button onClick={() => setShowTaskForm(!showTaskForm)}>
        {showTaskForm ? "Cancel" : "+ Create New Task"}
      </button>

      {showTaskForm && (
        <div className="create-task-form" style={{ marginTop: "1.5rem" }}>
          <h2>Create New Task</h2>
          <form onSubmit={handleCreateTask}>
            <div className="form-group">
              <label>Title</label>
              <input
                type="text"
                placeholder="Task title"
                value={newTask.title}
                onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
                required
              />
            </div>

            <div className="form-group">
              <label>Description</label>
              <textarea
                placeholder="Description"
                value={newTask.description}
                onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
              />
            </div>

            <div className="form-group">
              <label>Priority</label>
              <select
                value={newTask.priority}
                onChange={(e) => setNewTask({ ...newTask, priority: e.target.value })}
              >
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
                <option value="URGENT">Urgent</option>
              </select>
            </div>

            <div className="form-group">
              <label>Points</label>
              <input
                type="number"
                placeholder="Points"
                value={newTask.points}
                onChange={(e) => setNewTask({ ...newTask, points: parseInt(e.target.value) })}
              />
            </div>

            <div className="form-group">
              <label>Due Date</label>
              <input
                type="date"
                value={newTask.dueDate}
                onChange={(e) => setNewTask({ ...newTask, dueDate: e.target.value })}
              />
            </div>

            <div className="form-group">
              <label>Assign To</label>
              <select
                value={newTask.assignedToId || ""}
                onChange={(e) => setNewTask({ ...newTask, assignedToId: e.target.value ? parseInt(e.target.value) : null })}
              >
                <option value="">Unassigned (Mission Board)</option>
                {users.map(user => (
                  <option key={user.id} value={user.id}>{user.name}</option>
                ))}
              </select>
            </div>

            <button type="submit" className="btn btn-primary">Create Task</button>
          </form>
        </div>
      )}

      <h2 style={{ marginTop: "2rem" }}>All Tasks</h2>
      <div className="admin-task-list">
        {tasks.map(task => (
          <div key={task.id} className="admin-task-item">
            <div>
              <div className="admin-task-name">{task.title}</div>
              <div className="admin-task-meta">
                Assigned to: {task.assignedTo?.username || "Unassigned"}
              </div>
            </div>
            <span className={`status-pill s-${task.status.toLowerCase().replace("_", "")}`}>
              {task.status}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default AdminPanel;