function TaskCard ({ task, onComplete, onClaim, showCompleteButton = true, showClaimButton = false }) {
    const handleClaimClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        onClaim(task.id);
    };

    const handleCompleteClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        onComplete(task.id);
    };

    return (
        <div className={`task-card ${task.priority.toLowerCase()}`}>
            <h3>{task.title}</h3>
            <p>{task.description}</p>
            <p>Status: {task.status}</p>
            <p>Priority: {task.priority}</p>
            <p>Points: {task.points}</p>

            <p className={`status ${task.status.toLowerCase()}`}>{task.status}</p>

             {showClaimButton && onClaim && task.status !== "COMPLETED" && (
                <button onClick={handleClaimClick} className="claim-btn">
                    Claim Mission
                </button>
            )}

            {/* Show Complete button for Dashboard */}
            {showCompleteButton && onComplete && task.status !== "COMPLETED" && (
                <button onClick={handleCompleteClick} className="complete-btn">
                    Complete
                </button>
            )}
        </div>
    );
}

export default TaskCard;
