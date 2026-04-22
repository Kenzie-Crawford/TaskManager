import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const PARTICLES = Array.from({ length: 30 }, (_, i) => ({
  id: i,
  x: Math.random() * 100,
  y: Math.random() * 100,
  size: Math.random() * 2.5 + 1,
  duration: Math.random() * 14 + 8,
  delay: Math.random() * 8,
}));

export default function HeroPage() {
  const navigate = useNavigate();
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const t = setTimeout(() => setVisible(true), 80);
    return () => clearTimeout(t);
  }, []);

  return (
    <div style={styles.page}>

      {/* Floating particles */}
      <div style={styles.particles} aria-hidden="true">
        {PARTICLES.map((p) => (
          <span
            key={p.id}
            style={{
              ...styles.particle,
              left: `${p.x}%`,
              top: `${p.y}%`,
              width: p.size,
              height: p.size,
              animationDuration: `${p.duration}s`,
              animationDelay: `${p.delay}s`,
            }}
          />
        ))}
      </div>

      {/* Subtle grid */}
      <div style={styles.grid} aria-hidden="true" />

      {/* Center glow */}
      <div style={styles.glow} aria-hidden="true" />

      {/* Content */}
      <div style={styles.content}>

        {/* Live badge */}
        <div style={{
          ...styles.badge,
          opacity: visible ? 1 : 0,
          transform: visible ? "translateY(0)" : "translateY(12px)",
          transition: "opacity 0.5s ease, transform 0.5s ease",
        }}>
          <span style={styles.badgeDot} />
          Gamified Task Management
        </div>

        {/* Heading */}
        <h1 style={{
          ...styles.heading,
          opacity: visible ? 1 : 0,
          transform: visible ? "translateY(0)" : "translateY(24px)",
          transition: "opacity 0.6s 0.1s ease, transform 0.6s 0.1s ease",
        }}>
          Turn Work Into
          <br />
          <span style={styles.accent}>Missions.</span>
        </h1>

        {/* Subtext */}
        <p style={{
          ...styles.sub,
          opacity: visible ? 1 : 0,
          transform: visible ? "translateY(0)" : "translateY(16px)",
          transition: "opacity 0.6s 0.2s ease, transform 0.6s 0.2s ease",
        }}>
          Earn XP, unlock achievements, and climb the leaderboard —
          one completed task at a time.
        </p>

        {/* CTA Buttons */}
        <div style={{
          ...styles.buttons,
          opacity: visible ? 1 : 0,
          transform: visible ? "translateY(0)" : "translateY(14px)",
          transition: "opacity 0.6s 0.32s ease, transform 0.6s 0.32s ease",
        }}>
          <button
            style={styles.btnPrimary}
            onClick={() => navigate("/register")}
            onMouseEnter={(e) => {
              e.currentTarget.style.background = "#4db8f0";
              e.currentTarget.style.boxShadow = "0 0 36px rgba(56,166,232,0.6)";
              e.currentTarget.style.transform = "translateY(-2px) scale(1.02)";
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.background = "#38a6e8";
              e.currentTarget.style.boxShadow = "0 0 24px rgba(56,166,232,0.4)";
              e.currentTarget.style.transform = "translateY(0) scale(1)";
            }}
          >
            Get Started →
          </button>

          <button
            style={styles.btnSecondary}
            onClick={() => navigate("/login")}
            onMouseEnter={(e) => {
              e.currentTarget.style.background = "rgba(56,166,232,0.1)";
              e.currentTarget.style.borderColor = "#38a6e8";
              e.currentTarget.style.color = "#e8eaf0";
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.background = "transparent";
              e.currentTarget.style.borderColor = "rgba(56,166,232,0.3)";
              e.currentTarget.style.color = "#9ca3af";
            }}
          >
            Sign In
          </button>
        </div>
      </div>

      <style>{`
        @keyframes floatUp {
          0%   { transform: translateY(0)     scale(1);    opacity: 0.5; }
          50%  { transform: translateY(-20px) scale(1.2);  opacity: 1;   }
          100% { transform: translateY(0)     scale(1);    opacity: 0.5; }
        }
        @keyframes pulse {
          0%, 100% { opacity: 1; }
          50%       { opacity: 0.35; }
        }
      `}</style>
    </div>
  );
}

const styles = {
  page: {
    position: "relative",
    minHeight: "100vh",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    background: "linear-gradient(160deg, #0d1117 0%, #0d1a2e 100%)",
    overflow: "hidden",
    fontFamily: "system-ui, 'Segoe UI', Roboto, sans-serif",
  },
  particles: {
    position: "absolute",
    inset: 0,
    pointerEvents: "none",
  },
  particle: {
    position: "absolute",
    borderRadius: "50%",
    background: "rgba(56,166,232,0.5)",
    animation: "floatUp linear infinite",
  },
  grid: {
    position: "absolute",
    inset: 0,
    pointerEvents: "none",
    backgroundImage:
      "linear-gradient(rgba(56,166,232,0.045) 1px, transparent 1px), linear-gradient(90deg, rgba(56,166,232,0.045) 1px, transparent 1px)",
    backgroundSize: "52px 52px",
  },
  glow: {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 600,
    height: 400,
    background: "radial-gradient(ellipse, rgba(56,166,232,0.1) 0%, transparent 68%)",
    pointerEvents: "none",
  },
  content: {
    position: "relative",
    zIndex: 1,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    textAlign: "center",
    padding: "2rem",
    maxWidth: "700px",
  },
  badge: {
    display: "inline-flex",
    alignItems: "center",
    gap: "0.45rem",
    background: "rgba(56,166,232,0.1)",
    border: "1px solid rgba(56,166,232,0.28)",
    borderRadius: "999px",
    padding: "0.35rem 1rem",
    fontSize: "0.75rem",
    fontWeight: 600,
    color: "#38a6e8",
    letterSpacing: "0.6px",
    textTransform: "uppercase",
    marginBottom: "1.75rem",
  },
  badgeDot: {
    display: "inline-block",
    width: 7,
    height: 7,
    borderRadius: "50%",
    background: "#38a6e8",
    animation: "pulse 1.8s ease-in-out infinite",
  },
  heading: {
    fontSize: "clamp(2.8rem, 7vw, 5rem)",
    fontWeight: 800,
    lineHeight: 1.08,
    letterSpacing: "-2px",
    color: "#e8eaf0",
    marginBottom: "1.5rem",
  },
  accent: {
    color: "#38a6e8",
    textShadow: "0 0 48px rgba(56,166,232,0.45)",
  },
  sub: {
    fontSize: "1.1rem",
    lineHeight: 1.7,
    color: "#9ca3af",
    maxWidth: "480px",
    marginBottom: "2.75rem",
  },
  buttons: {
    display: "flex",
    gap: "1rem",
    flexWrap: "wrap",
    justifyContent: "center",
  },
  btnPrimary: {
    background: "#38a6e8",
    color: "#0d1117",
    border: "none",
    borderRadius: "8px",
    padding: "0.75rem 2rem",
    fontSize: "1rem",
    fontWeight: 700,
    cursor: "pointer",
    boxShadow: "0 0 24px rgba(56,166,232,0.4)",
    transition: "background 0.2s, box-shadow 0.2s, transform 0.2s",
    letterSpacing: "0.2px",
  },
  btnSecondary: {
    background: "transparent",
    color: "#9ca3af",
    border: "1px solid rgba(56,166,232,0.3)",
    borderRadius: "8px",
    padding: "0.75rem 2rem",
    fontSize: "1rem",
    fontWeight: 600,
    cursor: "pointer",
    transition: "background 0.2s, border-color 0.2s, color 0.2s",
  },
};
