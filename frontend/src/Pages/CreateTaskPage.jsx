import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../Services/api";
import CreateTaskForm from "../Components/CreateTaskForm";
import LoadingSpinner from "../Components/LoadingSpinner";
import ErrorMessage from "../Components/ErrorMessage";      


function CreateTaskPage() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();

    const userRole = localStorage.getItem("userRole");

    // Redirect if not manager/admin
    useEffect(() => {
        if (userRole !== "MANAGER" && userRole !== "ADMIN") {
            navigate("/dashboard");
        }
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        const res = await API.get("/users");
        setUsers(res.data);
    };

    const handleSubmit = async (formData) => {
        try {
            setLoading(true);
            setError("");
            const createdById = localStorage.getItem("userId");
            await API.post("/tasks", { ...formData, createdById });
            setSuccess("Task created successfully!");
            setTimeout(() => navigate("/admin"), 1500);
        } catch (err) {
            setError("Failed to create task");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="create-task-container">
            <h1>Create New Mission</h1>
            {error && <ErrorMessage message={error} onRetry={handleSubmit} />}
            {success && <p>{success}</p>}
            <div className="create-task-form">
                <CreateTaskForm
                    onSubmit={handleSubmit}
                    users={users}
                    loading={loading}
                />
            </div>
        </div>
    );
}

export default CreateTaskPage;