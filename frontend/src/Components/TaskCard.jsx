function TaskCard ({ task, onComplete }) {
    return (
        <div className={`task-card ${task.priority.toLowerCase()}`}>
            <h3>{task.title}</h3>
            <p>{task.description}</p>
            <p>Status: {task.status}</p>
            <p>Priority: {task.priority}</p>
            <p>Points: {task.points}</p>

            <p className={`status ${task.status.toLowerCase()}`}>{task.status}</p>

            {task.status !== "COMPLETED" && (
        <button onClick={() => onComplete(task.id)}>
          Complete
        </button>

        )}
        </div>
    )
}
export default TaskCard;
