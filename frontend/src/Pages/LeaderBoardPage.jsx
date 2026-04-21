import { useState, useEffect } from "react";
import { getLeaderboard } from "../Services/achievementService";

function LeaderboardPage() {
    const [leaderboard, setLeaderboard] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const currentUserId = localStorage.getItem("userId");

    useEffect(() => {
        fetchLeaderboard();
    }, []);

    const fetchLeaderboard = async () => {
        try {
            const res = await getLeaderboard(20);
            setLeaderboard(res.data);
        } catch (err) {
            setError("Failed to load leaderboard");
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const getRankMedal = (rank) => {
        if (rank === 1) return "🥇";
        if (rank === 2) return "🥈";
        if (rank === 3) return "🥉";
        return `#${rank}`;
    };

    if (loading) return <div>Loading leaderboard...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div>
            <h1>Leaderboard</h1>
            <p>Top performers ranked by total points</p>

            {leaderboard.length === 0 ? (
                <p>No users found.</p>
            ) : (
                <table>
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Name</th>
                            <th>Level</th>
                            <th>Points</th>
                            <th>Achievements</th>
                        </tr>
                    </thead>
                    <tbody>
                        {leaderboard.map((entry) => (
                            <tr
                                key={entry.userId}
                                className={
                                    entry.userId == currentUserId ? "current-user-row" : ""
                                }
                            >
                                <td>{getRankMedal(entry.rank)}</td>
                                <td>
                                    {entry.name}
                                    {entry.userId == currentUserId && " (you)"}
                                </td>
                                <td>Lvl {entry.level}</td>
                                <td>{entry.totalPoints} pts</td>
                                <td>{entry.achievementCount} 🏆</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default LeaderboardPage;