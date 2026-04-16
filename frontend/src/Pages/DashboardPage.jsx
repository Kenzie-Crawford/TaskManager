import React from "react";
import { useEffect } from "react";
import { getCurrentUser } from "../Services/authService";
import { useState } from "react";



export default function DashboardPage() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const res = await getCurrentUser();
                setUser(res.data);
            } catch (err) {
                console.error("Failed to load user");
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!user) {
        return <div>Failed to load user data</div>;
    }


return (
    <div>
        <h1>Welcome {user.name}</h1>
        <p>Level: {user.level}</p>
        <p>Points: {user.totalPoints}</p>
    </div>
);
}
