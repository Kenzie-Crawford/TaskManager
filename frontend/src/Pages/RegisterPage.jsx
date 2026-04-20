import { useState } from "react";
import { register } from "../Services/authService";
import { useNavigate, Link } from "react-router-dom";

function RegisterPage() {
  const [form, setForm] = useState({
    username: "",
    email: "",
    name: "",
    password: "",
    confirmPassword: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
    setError("");
  };

  const validateForm = () => {
    if (!form.username || !form.email || !form.name || !form.password || !form.confirmPassword) {
      setError("All fields are required");
      return false;
    }
    
    if (form.password !== form.confirmPassword) {
      setError("Passwords do not match");
      return false;
    }
    
    if (form.password.length < 6) {
      setError("Password must be at least 6 characters");
      return false;
    }
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email)) {
      setError("Please enter a valid email address");
      return false;
    }
    
    if (form.username.length < 3) {
      setError("Username must be at least 3 characters");
      return false;
    }
    
    if (form.name.length < 2) {
      setError("Name must be at least 2 characters");
      return false;
    }
    
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;

    setLoading(true);
    setError("");
    setSuccess("");

    try {
      const registrationData = {
        username: form.username,
        email: form.email,
        name: form.name,
        password: form.password,
      };

      const response = await register(registrationData);
      
      if (response.data.success) {
        setSuccess("Registration successful! Redirecting to login...");
        
        setForm({
          username: "",
          email: "",
          name: "",
          password: "",
          confirmPassword: "",
        });
        
        setTimeout(() => {
          navigate("/");
        }, 2000);
      } else {
        setError(response.data.message || "Registration failed");
      }
    } catch (err) {
      if (err.response?.data?.message) {
        setError(err.response.data.message);
      } else if (err.response?.status === 409) {
        setError("Username or email already exists");
      } else {
        setError("Registration failed. Please try again.");
      }
      console.error("Registration error:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1>Task Manager</h1>
      <p>Create your account</p>

      {error && (
        <div>
          <strong>Error:</strong> {error}
        </div>
      )}

      {success && (
        <div>
          <strong>Success!</strong> {success}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="name">Full Name *</label>
          <input
            id="name"
            type="text"
            name="name"
            value={form.name}
            onChange={handleChange}
            placeholder="John Doe"
            disabled={loading}
          />
        </div>

        <div>
          <label htmlFor="username">Username *</label>
          <input
            id="username"
            type="text"
            name="username"
            value={form.username}
            onChange={handleChange}
            placeholder="johndoe"
            disabled={loading}
          />
          <small>Minimum 3 characters</small>
        </div>

        <div>
          <label htmlFor="email">Email Address *</label>
          <input
            id="email"
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            placeholder="john@example.com"
            disabled={loading}
          />
        </div>

        <div>
          <label htmlFor="password">Password *</label>
          <input
            id="password"
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            placeholder="••••••"
            disabled={loading}
          />
          <small>Minimum 6 characters</small>
        </div>

        <div>
          <label htmlFor="confirmPassword">Confirm Password *</label>
          <input
            id="confirmPassword"
            type="password"
            name="confirmPassword"
            value={form.confirmPassword}
            onChange={handleChange}
            placeholder="••••••"
            disabled={loading}
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Creating account..." : "Sign Up"}
        </button>
      </form>

      <p>
        Already have an account? <Link to="/">Sign In</Link>
      </p>
    </div>
  );
}

export default RegisterPage;