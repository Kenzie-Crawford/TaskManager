import { useState } from "react";
import LoadingSpinner from "./LoadingSpinner";
import ErrorMessage from "./ErrorMessage";




function CreateTaskForm({ onSubmit, users = [], loading = false }) {
    const [form, setForm] = useState({
        title: "",
        description: "",
        priority: "MEDIUM",
        points: 10,
        dueDate: "",
        assignedToId: null,
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(form);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Title</label>
                <input
                    name="title"
                    type="text"
                    value={form.title}
                    onChange={handleChange}
                    required
                />
            </div>

            <div>
                <label>Description</label>
                <textarea
                    name="description"
                    value={form.description}
                    onChange={handleChange}
                />
            </div>

            <div>
                <label>Priority</label>
                <select name="priority" value={form.priority} onChange={handleChange}>
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                    <option value="URGENT">Urgent</option>
                </select>
            </div>

            <div>
                <label>Points</label>
                <input
                    name="points"
                    type="number"
                    value={form.points}
                    onChange={handleChange}
                    min="1"
                />
            </div>

            <div>
                <label>Due Date</label>
                <input
                    name="dueDate"
                    type="date"
                    value={form.dueDate}
                    onChange={handleChange}
                />
            </div>

            <div>
                <label>Assign To</label>
                <select
                    name="assignedToId"
                    value={form.assignedToId || ""}
                    onChange={(e) =>
                        setForm({
                            ...form,
                            assignedToId: e.target.value ? parseInt(e.target.value) : null,
                        })
                    }
                >
                    <option value="">Unassigned (Mission Board)</option>
                    {users.map((user) => (
                        <option key={user.id} value={user.id}>
                            {user.name} (@{user.username})
                        </option>
                    ))}
                </select>
            </div>

            <button type="submit" disabled={loading}>
                {loading ? "Creating..." : "Create Task"}
            </button>
        </form>
    );
}

export default CreateTaskForm;