import React from "react";
import { useEffect } from "react";
import { getCurrentUser } from "../Services/authService";
import { useState } from "react";
import API from "../Services/api";
import TaskCard from "../Components/TaskCard";
import { completeTask } from "../Services/taskService";
import { Link } from "react-router-dom";


function DashboardPage() {
    const [user, setUser] = useState(null);
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const userRes = await API.get(`auth/me`);
                setUser(userRes.data);

                const tasksres = await API.get(`/tasks/user/${userRes.data.id}`);
                setTasks(tasksres.data);
            } catch (err) {
                console.error("Failed to load user or tasks");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!user) {
        return <div>Failed to load user data</div>;
    }

    const handleComplete = async (taskId) => {
        try {
            const userId = localStorage.getItem("userId");

            await completeTask(taskId, userId);

            // refresh tasks
            const res = await API.get(`/tasks/user/${userId}`);
            setTasks(res.data);

            const userRes = await API.get(`auth/me`);
            setUser(userRes.data);

        } catch (err) {
            console.error("Error completing task", err);
        }
    };


    return (
        <div>
            <h1>Welcome {user.name}</h1>
            <p>Level: {user.level}</p>
            <p>Points: {user.totalPoints}</p>
            <h2>Your Tasks</h2>
            {tasks.length === 0 ? (
                <p>No tasks assigned</p>
            ) : (
                tasks.map(task => (
                    <Link
                        key={task.id}
                        to={`/tasks/${task.id}`}
                        style={{ textDecoration: "none", color: "inherit" }} // Removes link styling
                    >
                        <TaskCard
                            task={task}
                            onComplete={handleComplete}
                        />
                    </Link>
                ))
            )}
        </div>
    );
}

export default DashboardPage;
