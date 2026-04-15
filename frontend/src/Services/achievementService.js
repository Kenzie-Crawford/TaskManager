import API from "./api";

export const getUserAchievements = (userId) =>
  API.get(`/achievements/user/${userId}`);

export const getAvailableAchievements = (userId) =>
  API.get(`/achievements/user/${userId}/available`);

export const checkAchievements = (userId) =>
  API.post(`/achievements/user/${userId}/check`);