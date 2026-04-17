import API from "./api";

export const getTasks = () => API.get("/tasks");

export const getUnassignedTasks = () =>
  API.get("/tasks/mission-board");

export const claimTask = (taskId, userId) =>
  API.patch(`/tasks/mission-board/${taskId}/claim?userId=${userId}`);

export const completeTask = (taskId, userId) =>
  API.patch(`/tasks/${taskId}/complete?userId=${userId}`);