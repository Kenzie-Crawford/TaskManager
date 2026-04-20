import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getUnassignedTasks, claimTask } from "../Services/taskService";
import TaskCard from "../Components/TaskCard";


function MissionBoardPage() {
    const [tasks, setTasks] = useState([]);

    useEffect(() => {
        fetchTasks();
    }, []);

    const fetchTasks = async () => {
        try {
            const res = await getUnassignedTasks();
            setTasks(res.data);
        } catch (err) {
            console.error("Failed to load tasks", err);
        }
    };

    const handleClaim = async (taskId) => {
        try {
            const userId = localStorage.getItem("userId");
            await claimTask(taskId, userId);
            fetchTasks(); // refresh task list
        } catch (err) {
            console.error("Failed to claim task", err);
        }
    };

    

    return (
        <div className="mission-board-container">
            <h1>Mission Board</h1>

            {tasks.length === 0 ? (
                <p>No available missions right now</p>
            ) : (
                tasks.map((task) => (
                    <Link
                        key={task.id}
                        to={`/tasks/${task.id}`}
                        style={{ textDecoration: "none", color: "inherit" }}
                    >
                        <TaskCard
                            task={task}
                            onClaim={handleClaim}
                            showCompleteButton={false}
                            showClaimButton={true}
                        />
                    </Link>
                ))
            )}
        </div>
    );
}

    export default MissionBoardPage;

