function TaskCard ({ task, onComplete, onStart, onClaim, showCompleteButton = true, showClaimButton = false }) {

    const handleClaimClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        onClaim(task.id);
    };

    const handleStartClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        onStart(task.id);
    };

    const handleCompleteClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        onComplete(task.id);
    };

    const isPending    = task.status === "PENDING";
    const isInProgress = task.status === "IN_PROGRESS";
    const isCompleted  = task.status === "COMPLETED";

    return (
        <div className={`task-card ${task.priority.toLowerCase()}`}>
            <h3>{task.title}</h3>
            <p>{task.description}</p>
            <p>Priority: {task.priority}</p>
            <p>Points: {task.points}</p>

            <p className={`status ${task.status.toLowerCase()}`}>{task.status}</p>

            {/* Claim button — Mission Board only */}
            {showClaimButton && onClaim && !isCompleted && (
                <button onClick={handleClaimClick} className="claim-btn">
                    Claim Mission
                </button>
            )}

            {/* Start button — Dashboard, PENDING tasks only */}
            {showCompleteButton && onStart && isPending && (
                <button onClick={handleStartClick} className="claim-btn">
                    Start Mission
                </button>
            )}

            {/* Complete button — Dashboard, IN_PROGRESS tasks only */}
            {showCompleteButton && onComplete && isInProgress && (
                <button onClick={handleCompleteClick} className="complete-btn">
                    Mark As Completed
                </button>
            )}
        </div>
    );
}

export default TaskCard;