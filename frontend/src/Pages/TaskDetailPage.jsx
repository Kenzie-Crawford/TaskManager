import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import API from "../Services/api";

function TaskDetailPage() {
  const { id } = useParams(); // Get task ID from URL
  const navigate = useNavigate();
  const [task, setTask] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({
    title: "",
    description: "",
    status: "",
    priority: "",
    points: "",
    dueDate: ""
  });

  // Get current user role for edit/delete permissions
  const userRole = localStorage.getItem("userRole");
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    fetchTask();
  }, [id]);

  const fetchTask = async () => {
    try {
      const response = await API.get(`/tasks/${id}`);
      setTask(response.data);
      setEditForm({
        title: response.data.title || "",
        description: response.data.description || "",
        status: response.data.status || "PENDING",
        priority: response.data.priority || "MEDIUM",
        points: response.data.points || 10,
        dueDate: response.data.dueDate || ""
      });
    } catch (err) {
      setError("Failed to load task");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      const response = await API.put(`/tasks/${id}`, editForm);
      setTask(response.data);
      setIsEditing(false);
      alert("Task updated successfully!");
    } catch (err) {
      setError("Failed to update task");
      console.error(err);
    }
  };

  const handleDelete = async () => {
    if (window.confirm("Are you sure you want to delete this task?")) {
      try {
        await API.delete(`/tasks/${id}`);
        alert("Task deleted successfully!");
        navigate("/dashboard");
      } catch (err) {
        setError("Failed to delete task");
        console.error(err);
      }
    }
  };

  const handleComplete = async () => {
    try {
      await API.patch(`/tasks/${id}/complete?userId=${userId}`);
      alert("Task completed! You earned " + task.points + " points!");
      fetchTask(); // Refresh task data
    } catch (err) {
      setError("Failed to complete task");
      console.error(err);
    }
  };

  if (loading) {
    return <div>Loading task...</div>;
  }

  if (error) {
    return (
      <div>
        <p style={{ color: "red" }}>Error: {error}</p>
        <button onClick={() => navigate("/dashboard")}>Back to Dashboard</button>
      </div>
    );
  }

  if (!task) {
    return <div>Task not found</div>;
  }

  // Check if current user is assigned to this task
  const isAssignedToMe = task.assignedTo && task.assignedTo.id == userId;
  
  // Check if user can edit/delete (MANAGER, ADMIN, or task creator)
  const canEdit = userRole === "MANAGER" || userRole === "ADMIN";

  return (
    <div>
      {/* Back button */}
      <button onClick={() => navigate(-1)}>← Back</button>

      {isEditing ? (
        // EDIT MODE - Show form
        <div>
          <h1>Edit Task</h1>
          <form onSubmit={handleUpdate}>
            <div>
              <label>Title *</label>
              <input
                type="text"
                value={editForm.title}
                onChange={(e) => setEditForm({...editForm, title: e.target.value})}
                required
              />
            </div>

            <div>
              <label>Description</label>
              <textarea
                value={editForm.description}
                onChange={(e) => setEditForm({...editForm, description: e.target.value})}
                rows="4"
              />
            </div>

            <div>
              <label>Status</label>
              <select
                value={editForm.status}
                onChange={(e) => setEditForm({...editForm, status: e.target.value})}
              >
                <option value="PENDING">Pending</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="COMPLETED">Completed</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>

            <div>
              <label>Priority</label>
              <select
                value={editForm.priority}
                onChange={(e) => setEditForm({...editForm, priority: e.target.value})}
              >
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
                <option value="URGENT">Urgent</option>
              </select>
            </div>

            <div>
              <label>Points</label>
              <input
                type="number"
                value={editForm.points}
                onChange={(e) => setEditForm({...editForm, points: parseInt(e.target.value)})}
              />
            </div>

            <div>
              <label>Due Date</label>
              <input
                type="date"
                value={editForm.dueDate}
                onChange={(e) => setEditForm({...editForm, dueDate: e.target.value})}
              />
            </div>

            <button type="submit">Save Changes</button>
            <button type="button" onClick={() => setIsEditing(false)}>Cancel</button>
          </form>
        </div>
      ) : (
        // VIEW MODE - Show task details
        <div>
          <h1>{task.title}</h1>
          
          {/* Show Complete button only if task is assigned to current user and not completed */}
          {isAssignedToMe && task.status !== "COMPLETED" && (
            <button onClick={handleComplete}>✓ Complete Task</button>
          )}
          
          {/* Show Edit/Delete buttons for managers/admins */}
          {canEdit && (
            <div>
              <button onClick={() => setIsEditing(true)}>✏ Edit Task</button>
              <button onClick={handleDelete}>🗑 Delete Task</button>
            </div>
          )}

          <div>
            <p><strong>Description:</strong></p>
            <p>{task.description || "No description provided"}</p>
          </div>

          <div>
            <p><strong>Status:</strong> {task.status}</p>
            <p><strong>Priority:</strong> {task.priority}</p>
            <p><strong>Points:</strong> {task.points}</p>
            <p><strong>Due Date:</strong> {task.dueDate || "No due date"}</p>
          </div>

          <div>
            <p><strong>Assigned To:</strong> {task.assignedTo?.username || "Unassigned"}</p>
            <p><strong>Created By:</strong> {task.createdBy?.username || "Unknown"}</p>
          </div>

          {task.completedAt && (
            <p><strong>Completed At:</strong> {new Date(task.completedAt).toLocaleString()}</p>
          )}

          <p><strong>Created At:</strong> {new Date(task.createdAt).toLocaleString()}</p>
        </div>
      )}
    </div>
  );
}

export default TaskDetailPage;