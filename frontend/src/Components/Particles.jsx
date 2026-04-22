const PARTICLES = Array.from({ length: 30 }, (_, i) => ({
  id: i,
  x: Math.random() * 100,
  y: Math.random() * 100,
  size: Math.random() * 2.5 + 1,
  duration: Math.random() * 14 + 8,
  delay: Math.random() * 8,
}));

function Particles() {
  return (
    <>
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

      <style>{`
        @keyframes floatUp {
          0%   { transform: translateY(0)     scale(1);   opacity: 0.5; }
          50%  { transform: translateY(-20px) scale(1.2); opacity: 1;   }
          100% { transform: translateY(0)     scale(1);   opacity: 0.5; }
        }
      `}</style>
    </>
  );
}

const styles = {
  particles: {
    position: "fixed",
    inset: 0,
    pointerEvents: "none",
    zIndex: 0,
  },
  particle: {
    position: "absolute",
    borderRadius: "50%",
    background: "rgba(56,166,232,0.5)",
    animation: "floatUp linear infinite",
  },
};

export default Particles;