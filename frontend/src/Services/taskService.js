import API from "./api";

export const getTasks = () => API.get("/tasks");

export const getUnassignedTasks = () =>
  API.get("/tasks/unassigned");

export const claimTask = (taskId, userId) =>
  API.post(`/tasks/${taskId}/claim?userId=${userId}`);

export const completeTask = (taskId, userId) =>
  API.post(`/tasks/${taskId}/complete?userId=${userId}`);