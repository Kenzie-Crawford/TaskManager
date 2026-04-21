import API from "./api";

export const getUserAchievements = (userId) =>
  API.get(`/achievements/user/${userId}`);

export const getAvailableAchievements = (userId) =>
  API.get(`/achievements/user/${userId}/available`);

export const checkAchievements = (userId) =>
  API.post(`/achievements/user/${userId}/check`);

export const getAllAchievements = () => API.get("/achievements");

export const createAchievement = (data) => API.post("/achievements", data);

export const getAchievementProgress = (userId, achievementId) => 
  API.get(`/achievements/user/${userId}/progress/${achievementId}`);

export const getLeaderboard = (limit = 20) =>
    API.get(`/users/leaderboard?limit=${limit}`);

export const checkAndAwardAchievements = (userId) =>
  API.post(`/achievements/user/${userId}/check`);


