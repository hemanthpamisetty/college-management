// ---------------- LOGIN LOGIC ----------------
document.getElementById("loginForm").addEventListener("submit", async function(e){
    e.preventDefault();

    const regno = document.getElementById("textInput").value.trim();
    const password = document.getElementById("passwordInput").value.trim();
    const error = document.getElementById("error");

    // -------- VALIDATION --------
    if(regno === "" || password === ""){
        error.innerText = "All fields are required!";
        return;
    }

    if(password.length < 6){
        error.innerText = "Password must be at least 6 characters!";
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: regno, // Can be email or registration number
                password: password
            })
        });

        if (response.ok) {
            const data = await response.json();
            // -------- SESSION --------
            localStorage.setItem("isLoggedIn", "true");
            localStorage.setItem("regno", regno);
            localStorage.setItem("userName", data.name);
            localStorage.setItem("userRole", data.role);

            // -------- REDIRECT --------
            window.location.href = "dashbord.html";
        } else {
            const errData = await response.json();
            error.innerText = errData.message || "Invalid Registration Number or Password!";
        }
    } catch (err) {
        console.error(err);
        error.innerText = "Network error. Server might be down.";
    }
});
