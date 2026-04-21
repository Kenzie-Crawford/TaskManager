
function AchievementCard({ achievement, earned = false, progress = null }) {
    return (
        <div className={`achievement-card ${earned ? "earned" : "locked"}`}>
            <div className="achievement-icon">
                {achievement.badgeIcon || (earned ? "🏆" : "🔒")}
            </div>
            <div className="achievement-info">
                <h3>{achievement.achievementName || achievement.name}</h3>
                <p>{achievement.achievementDescription || achievement.description}</p>

                {earned && achievement.earnedAt && (
                    <p className="earned-date">
                        Earned: {new Date(achievement.earnedAt).toLocaleDateString()}
                    </p>
                )}

                {!earned && progress !== null && (
                    <div className="progress-bar-container">
                        <div
                            className="progress-bar"
                            style={{ width: `${progress}%` }}
                        />
                        <span>{progress}%</span>
                    </div>
                )}

                {!earned && (
                    <p className="criteria-hint">
                        Goal: {achievement.criteriaValue}{" "}
                        {achievement.criteriaType === "TASKS_COMPLETED"
                            ? "tasks completed"
                            : achievement.criteriaType === "TOTAL_POINTS"
                            ? "points earned"
                            : "achievements"}
                    </p>
                )}
            </div>
        </div>
    );
}

export default AchievementCard;