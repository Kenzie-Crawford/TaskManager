import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8081/api',

});
API.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");

    if(token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});
export default API;
//axios used so JWT is loaded in the header of each request, allowing for authentication and authorization on the backend.
//Automatically parses JSON responses from the backend, making it easier to work with the data in the frontend.
//Less boilerplate code compared to using the Fetch API, as it provides a simpler and more intuitive syntax for making HTTP requests.