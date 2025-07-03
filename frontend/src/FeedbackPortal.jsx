import React, { useState, useEffect } from "react";

// Cyberpunk Google Font
const cyberpunkFont = "https://fonts.googleapis.com/css2?family=Orbitron:wght@700&display=swap";

export default function FeedbackPortal() {
  const [form, setForm] = useState({ name: "", email: "", message: "" });
  const [status, setStatus] = useState(null);
  const [stats, setStats] = useState(null);

  useEffect(() => {
    fetch("/api/feedback/stats")
      .then(res => res.json())
      .then(setStats)
      .catch(() => setStats(null));
  }, []);

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setStatus(null);
    try {
      const res = await fetch("/api/feedback", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      if (res.ok) {
        setStatus("success");
        setForm({ name: "", email: "", message: "" });
      } else {
        setStatus("error");
      }
    } catch {
      setStatus("error");
    }
  };

  return (
    <div style={{ minHeight: "100vh", background: "linear-gradient(135deg, #09011a 60%, #1a0a2d 100%)", color: "#fff", fontFamily: "'Orbitron', Arial, sans-serif", padding: 0, margin: 0, position: "relative", overflow: "hidden" }}>
      <link href={cyberpunkFont} rel="stylesheet" />
      {/* Cyberpunk Glitch Circuit Pattern */}
      <svg style={{ position: "absolute", top: 0, left: 0, zIndex: 0, width: "100vw", height: "100vh", opacity: 0.08 }}>
        <rect x="10" y="10" width="90%" height="90%" rx="30" stroke="#39ff14" strokeWidth="2" fill="none" />
        <rect x="60" y="60" width="80%" height="80%" rx="20" stroke="#00fff7" strokeWidth="1.5" fill="none" />
        <rect x="120" y="120" width="70%" height="70%" rx="10" stroke="#ff00ea" strokeWidth="1" fill="none" />
      </svg>
      <header style={{ padding: "2rem 0 1rem 0", textAlign: "center", letterSpacing: "0.1em", fontSize: "2.5rem", color: "#39ff14", textShadow: "0 0 10px #39ff14, 0 0 20px #00fff7, 0 0 6px #ff00ea" }}>
        Feedback Portal
      </header>
      <form
        onSubmit={handleSubmit}
        style={{
          background: "rgba(20,20,40,0.95)",
          border: "2px solid #39ff14",
          borderRadius: "16px",
          boxShadow: "0 0 40px #00fff7, 0 0 10px #ff00ea",
          maxWidth: 420,
          margin: "2rem auto 1rem auto",
          padding: "2rem",
          display: "flex",
          flexDirection: "column",
          gap: "1.2rem",
          zIndex: 1,
          position: "relative",
          animation: "glowForm 1.5s infinite alternate"
        }}
      >
        <label style={{ fontWeight: 700, color: "#00fff7", letterSpacing: "0.05em" }}>
          Name
          <input
            name="name"
            value={form.name}
            onChange={handleChange}
            required
            style={inputStyle}
            maxLength={100}
            autoComplete="off"
          />
        </label>
        <label style={{ fontWeight: 700, color: "#00fff7", letterSpacing: "0.05em" }}>
          Email
          <input
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            required
            style={inputStyle}
            maxLength={100}
            autoComplete="off"
          />
        </label>
        <label style={{ fontWeight: 700, color: "#00fff7", letterSpacing: "0.05em" }}>
          Message
          <textarea
            name="message"
            value={form.message}
            onChange={handleChange}
            required
            style={{ ...inputStyle, minHeight: 80, resize: "vertical" }}
            maxLength={1000}
            autoComplete="off"
          />
        </label>
        <button
          type="submit"
          style={{
            ...buttonStyle,
            background: "linear-gradient(90deg, #ff00ea 0%, #39ff14 100%)",
            color: "#000",
            fontWeight: 700,
            animation: "glowBtn 1s infinite alternate"
          }}
        >
          Submit
        </button>
        {status === "success" && (
          <div style={{ color: "#39ff14", textShadow: "0 0 8px #39ff14" }}>
            Feedback submitted! Thank you!
          </div>
        )}
        {status === "error" && (
          <div style={{ color: "#ff00ea", textShadow: "0 0 8px #ff00ea" }}>
            Submission failed. Please try again.
          </div>
        )}
      </form>
      <section style={{ maxWidth: 420, margin: "1rem auto", textAlign: "center", background: "rgba(20,20,40,0.7)", borderRadius: "12px", padding: "1rem", border: "1px solid #00fff7", color: "#00fff7", zIndex: 1, position: "relative" }}>
        <h3 style={{ margin: 0, fontSize: "1.2rem", letterSpacing: "0.05em", color: "#ff00ea", textShadow: "0 0 8px #ff00ea" }}>Feedback Stats</h3>
        {stats ? (
          <div>
            <div>Total Feedbacks: <b>{stats.totalFeedbacks}</b></div>
            <div>Last Updated: <b>{stats.lastUpdated}</b></div>
          </div>
        ) : (
          <div>Loading stats...</div>
        )}
      </section>
      <footer style={{ textAlign: "center", marginTop: "2rem", color: "#ff00ea", opacity: 0.5, fontSize: "0.9rem", zIndex: 1, position: "relative" }}>
        &copy; {new Date().getFullYear()} Feedback Portal | Cyberpunk Theme
      </footer>
      {/* Keyframes for glowing effects */}
      <style>{`
        @keyframes glowForm {
          0% { box-shadow: 0 0 40px #00fff7, 0 0 10px #ff00ea; }
          100% { box-shadow: 0 0 60px #39ff14, 0 0 20px #ff00ea; }
        }
        @keyframes glowBtn {
          0% { box-shadow: 0 0 20px #ff00ea, 0 0 10px #39ff14; }
          100% { box-shadow: 0 0 40px #39ff14, 0 0 20px #ff00ea; }
        }
        input:focus, textarea:focus {
          border-color: #ff00ea !important;
          box-shadow: 0 0 10px #ff00ea inset !important;
        }
      `}</style>
    </div>
  );
}

const inputStyle = {
  width: "100%",
  padding: "0.7rem",
  borderRadius: "8px",
  border: "2px solid #00fff7",
  background: "#181830",
  color: "#fff",
  marginTop: "0.2rem",
  fontFamily: "'Orbitron', Arial, sans-serif",
  fontSize: "1rem",
  outline: "none",
  boxShadow: "0 0 10px #00fff7 inset",
  transition: "border 0.2s, box-shadow 0.2s",
};

const buttonStyle = {
  padding: "0.8rem 0",
  border: "none",
  borderRadius: "8px",
  fontSize: "1.1rem",
  letterSpacing: "0.1em",
  boxShadow: "0 0 20px #ff00ea, 0 0 10px #39ff14",
  cursor: "pointer",
  transition: "background 0.2s, color 0.2s",
};
