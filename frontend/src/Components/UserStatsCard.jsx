
function UserStatsCard({ user, achievementCount = 0 }) {
    const getLevelTitle = (level) => {
        if (level < 3) return "Rookie";
        if (level < 6) return "Apprentice";
        if (level < 10) return "Veteran";
        return "Elite";
    };

    const pointsIntoCurrentLevel = user.totalPoints % 100;
    const pointsToNextLevel = 100 - pointsIntoCurrentLevel;

    return (
        <div className="user-stats-card">
            <div className="user-stats-header">
                <h2>{user.name}</h2>
                <span className="user-role">{user.role}</span>
            </div>

            <div className="user-stats-grid">
                <div className="stat-item">
                    <span className="stat-value">{user.totalPoints} </span>
                    <span className="stat-label">Total Points</span>
                </div>

                <div className="stat-item">
                    <span className="stat-value">Lvl {user.level} </span>
                    <span className="stat-label">{getLevelTitle(user.level)}</span>
                </div>

                <div className="stat-item">
                    <span className="stat-value">{achievementCount} </span>
                    <span className="stat-label">Achievements</span>
                </div>
            </div>

            <div className="level-progress">
                <p>{pointsToNextLevel} points to next level</p>
                <div className="progress-bar-container">
                    <div
                        className="progress-bar"
                        style={{ width: `${pointsIntoCurrentLevel}%` }}
                    />
                </div>
            </div>
        </div>
    );
}

export default UserStatsCard;