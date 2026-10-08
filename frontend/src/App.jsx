import { useState } from "react";
import "./App.css";

function App() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loggedIn, setLoggedIn] = useState(false);

  const [uploadedFiles, setUploadedFiles] = useState([]);

  const [uploading, setUploading] = useState(false);
  const [loadingResumes, setLoadingResumes] = useState(false);

  // =========================
  // LOGIN
  // =========================

  const handleLogin = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/auth/login",
        {
          method: "POST",

          headers: {
            "Content-Type": "application/json",
          },

          body: JSON.stringify({
            email: email,
            password: password,
          }),
        }
      );

      const data = await response.text();

      if (response.ok) {
        const token = data.replace(
          "Login successful. Token: ",
          ""
        );

        localStorage.setItem("token", token);

        setLoggedIn(true);

        await loadResumes(token);
      } else {
        alert("Invalid email or password");
      }
    } catch (error) {
      console.error(error);

      alert(
        "Cannot connect to backend. Make sure Spring Boot is running."
      );
    }
  };

  // =========================
  // LOAD MY RESUMES
  // =========================

  const loadResumes = async (savedToken = null) => {
    try {
      setLoadingResumes(true);

      const token =
        savedToken || localStorage.getItem("token");

      const response = await fetch(
        "http://localhost:8080/api/resumes",
        {
          method: "GET",

          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();

        console.log("My Resumes:", data);

        const resumes = data.map((resume) => ({
          id: resume.id,
          name: resume.fileName || "Resume",
          response: resume.resumeText || "",
        }));

        setUploadedFiles(resumes);
      } else {
        console.log(
          "Could not load resumes:",
          response.status
        );

        alert("Could not load resumes");
      }
    } catch (error) {
      console.error(error);

      alert(
        "Cannot connect to backend. Make sure Spring Boot is running."
      );
    } finally {
      setLoadingResumes(false);
    }
  };

  // =========================
  // MY RESUMES
  // =========================

  const handleMyResumes = async () => {
    await loadResumes();
  };

  // =========================
  // UPLOAD RESUME
  // =========================

  const handleFileChange = async (event) => {
    const file = event.target.files[0];

    if (!file) {
      return;
    }

    if (file.type !== "application/pdf") {
      alert("Please select a PDF file.");

      event.target.value = "";

      return;
    }

    try {
      setUploading(true);

      const formData = new FormData();

      formData.append("file", file);

      const token = localStorage.getItem("token");

      const response = await fetch(
        "http://localhost:8080/api/pdf/upload",
        {
          method: "POST",

          headers: {
            Authorization: `Bearer ${token}`,
          },

          body: formData,
        }
      );

      const data = await response.text();

      if (response.ok) {
        alert("Resume uploaded successfully!");

        await loadResumes();
      } else {
        alert("Resume upload failed");

        console.log(
          "Upload error:",
          data
        );
      }
    } catch (error) {
      console.error(error);

      alert(
        "Cannot connect to backend. Make sure Spring Boot is running."
      );
    } finally {
      setUploading(false);

      event.target.value = "";
    }
  };

  // =========================
  // VIEW RESUME
  // =========================

  const handleViewResume = (resume) => {
    if (!resume.response) {
      alert("No resume text available.");
      return;
    }

    alert(resume.response);
  };

  // =========================
  // DELETE RESUME
  // =========================

  const handleDeleteResume = async (id) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this resume?"
    );

    if (!confirmDelete) {
      return;
    }

    try {
      const token = localStorage.getItem("token");

      const response = await fetch(
        `http://localhost:8080/api/resumes/${id}`,
        {
          method: "DELETE",

          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        alert("Resume deleted successfully!");

        await loadResumes();
      } else {
        alert("Resume deletion failed.");
      }
    } catch (error) {
      console.error(error);

      alert(
        "Cannot connect to backend. Make sure Spring Boot is running."
      );
    }
  };

  // =========================
  // LOGOUT
  // =========================

  const handleLogout = () => {
    localStorage.removeItem("token");

    setLoggedIn(false);

    setEmail("");

    setPassword("");

    setUploadedFiles([]);
  };

  // =========================
  // DASHBOARD
  // =========================

  if (loggedIn) {
    return (
      <div className="dashboard">

        <div className="dashboard-box">

          <h1>
            Resume Analyzer
          </h1>

          <p className="welcome">
            Welcome to your dashboard!
          </p>

          {/* =========================
              DASHBOARD BUTTONS
          ========================= */}

          <div className="dashboard-actions">

            {/* UPLOAD RESUME */}

            <label className="dashboard-button">

              📄&nbsp;&nbsp;

              {uploading
                ? "Uploading..."
                : "Upload Resume"}

              <input
                type="file"
                accept=".pdf"
                onChange={handleFileChange}
                style={{
                  display: "none",
                }}
                disabled={uploading}
              />

            </label>

            {/* MY RESUMES */}

            <button
              className="dashboard-button"
              onClick={handleMyResumes}
              disabled={loadingResumes}
            >

              📋&nbsp;&nbsp;

              {loadingResumes
                ? "Loading..."
                : "My Resumes"}

            </button>

            {/* LOGOUT */}

            <button
              className="logout-button"
              onClick={handleLogout}
            >

              🚪&nbsp;&nbsp;

              Logout

            </button>

          </div>

          {/* =========================
              MY RESUMES LIST
          ========================= */}

          {uploadedFiles.length > 0 && (

            <div className="uploaded-resumes">

              <h2>
                My Resumes
              </h2>

              {uploadedFiles.map(
                (resume, index) => (

                  <div
                    className="resume-item"
                    key={`${resume.id || resume.name}-${index}`}
                  >

                    <span>
                      📄 {resume.name}
                    </span>

                    {/* VIEW BUTTON */}

                    <button
                      className="view-button"
                      onClick={() =>
                        handleViewResume(resume)
                      }
                    >
                      View Resume
                    </button>

                    {/* DELETE BUTTON */}

                    <button
                      className="delete-button"
                      onClick={() =>
                        handleDeleteResume(resume.id)
                      }
                    >
                      Delete
                    </button>

                  </div>

                )
              )}

            </div>

          )}

          {/* NO RESUMES */}

          {uploadedFiles.length === 0 &&
            !loadingResumes && (

              <p className="no-resumes">
                No resumes uploaded yet.
              </p>

            )}

        </div>

      </div>
    );
  }

  // =========================
  // LOGIN PAGE
  // =========================

  return (
    <div className="login-container">

      <div className="login-box">

        <h1>
          Resume Analyzer
        </h1>

        <p className="subtitle">
          Login to your account
        </p>

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) =>
            setEmail(e.target.value)
          }
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) =>
            setPassword(e.target.value)
          }
        />

        <button
          onClick={handleLogin}
        >
          Login
        </button>

        <p className="register-text">

          Don't have an account?

          <span>
            {" "}Register
          </span>

        </p>

      </div>

    </div>
  );
}

export default App;