import { useState, useEffect } from "react";
import AchievementCard from "../Components/AchievementCard";
import {
    getUserAchievements,
    getAvailableAchievements,
    getAchievementProgress,
    checkAndAwardAchievements,
} from "../Services/achievementService";

function AchievementsPage() {
    const [earned, setEarned] = useState([]);
    const [available, setAvailable] = useState([]);
    const [progressMap, setProgressMap] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [checking, setChecking] = useState(false);
    const [checkMessage, setCheckMessage] = useState("");

    const userId = localStorage.getItem("userId");

    useEffect(() => {
        fetchAchievements();
    }, []);

    const fetchAchievements = async () => {
        try {
            setLoading(true);
            const [earnedRes, availableRes] = await Promise.all([
                getUserAchievements(userId),
                getAvailableAchievements(userId),
            ]);
            setEarned(earnedRes.data);
            setAvailable(availableRes.data);

            const progressResults = await Promise.all(
                availableRes.data.map((a) =>
                    getAchievementProgress(userId, a.id).then((res) => ({
                        id: a.id,
                        progress: res.data,
                    }))
                )
            );
            const map = {};
            progressResults.forEach(({ id, progress }) => {
                map[id] = progress;
            });
            setProgressMap(map);
        } catch (err) {
            setError("Failed to load achievements");
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleCheckAchievements = async () => {
        try {
            setChecking(true);
            setCheckMessage("");
            await checkAndAwardAchievements(userId);
            setCheckMessage("Achievements checked! Refreshing...");
            await fetchAchievements();
            setCheckMessage("Done! Your achievements are up to date.");
        } catch (err) {
            setCheckMessage("Failed to check achievements.");
            console.error(err);
        } finally {
            setChecking(false);
        }
    };

    if (loading) return <div>Loading achievements...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div>
            <h1>Achievements</h1>

            <button onClick={handleCheckAchievements} disabled={checking}>
                {checking ? "Checking..." : "Check for New Achievements"}
            </button>
            {checkMessage && <p>{checkMessage}</p>}

            <section>
                <h2>Earned ({earned.length})</h2>
                {earned.length === 0 ? (
                    <p>No achievements earned yet. Complete tasks to get started!</p>
                ) : (
                    earned.map((a) => (
                        <AchievementCard key={a.id} achievement={a} earned={true} />
                    ))
                )}
            </section>

            <section>
                <h2>In Progress ({available.length})</h2>
                {available.length === 0 ? (
                    <p>You've earned all available achievements!</p>
                ) : (
                    available.map((a) => (
                        <AchievementCard
                            key={a.id}
                            achievement={a}
                            earned={false}
                            progress={progressMap[a.id] ?? 0}
                        />
                    ))
                )}
            </section>
        </div>
    );
}

export default AchievementsPage;