function TaskCard ({ task, onComplete, onClaim, showCompleteButton = true, showClaimButton = false }) {
    return (
        <div className={`task-card ${task.priority.toLowerCase()}`}>
            <h3>{task.title}</h3>
            <p>{task.description}</p>
            <p>Status: {task.status}</p>
            <p>Priority: {task.priority}</p>
            <p>Points: {task.points}</p>

            <p className={`status ${task.status.toLowerCase()}`}>{task.status}</p>

             {showClaimButton && onClaim && task.status !== "COMPLETED" && (
                <button onClick={() => onClaim(task.id)} className="claim-btn">
                    Claim Mission
                </button>
            )}

            {/* Show Complete button for Dashboard */}
            {showCompleteButton && onComplete && task.status !== "COMPLETED" && (
                <button onClick={() => onComplete(task.id)} className="complete-btn">
                    Complete
                </button>
            )}
        </div>
    );
}

export default TaskCard;
