// ---------- REGISTER LOGIC ----------
document.getElementById("registerForm").addEventListener("submit", async function(e){
    e.preventDefault();

    const email = document.getElementById("emailInput").value.trim();
    const regno = document.getElementById("textInput").value.trim();
    const password = document.getElementById("passwordInput").value.trim();

    const error = document.getElementById("error");
    const success = document.getElementById("success");

    error.innerText = "";
    success.innerText = "";

    // ---------- VALIDATION ----------
    if(email === "" || regno === "" || password === ""){
        error.innerText = "All fields are required!";
        return;
    }

    if(!email.includes("@") || !email.includes(".")){
        error.innerText = "Enter a valid email address!";
        return;
    }

    if(password.length < 6){
        error.innerText = "Password must be at least 6 characters!";
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: regno, // Using regno as name for basic demo
                email: email,
                password: password,
                role: "STUDENT",
                registrationNumber: regno
            })
        });

        if (response.ok) {
            success.innerText = "Registration successful! Redirecting to login...";
            setTimeout(() => {
                window.location.href = "login.html";
            }, 1500);
        } else {
            const errData = await response.json();
            // Handle validation errors or normal error message
            if (errData.message) {
                error.innerText = errData.message;
            } else {
                error.innerText = "Registration failed! Check input constraints.";
            }
        }
    } catch (err) {
        console.error(err);
        error.innerText = "Network error. Server might be down.";
    }
});
