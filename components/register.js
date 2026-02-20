// ---------- HASH FUNCTION ----------
function hash(text){
    let hash = 0;
    for(let i = 0; i < text.length; i++){
        hash = ((hash << 5) - hash) + text.charCodeAt(i);
        hash |= 0;
    }
    return hash.toString();
}

// ---------- GET USERS ----------
function getUsers(){
    return JSON.parse(localStorage.getItem("users")) || [];
}

// ---------- SAVE USERS ----------
function saveUsers(users){
    localStorage.setItem("users", JSON.stringify(users));
}

// ---------- REGISTER LOGIC ----------
document.getElementById("registerForm").addEventListener("submit", function(e){
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

    let users = getUsers();

    // ---------- DUPLICATE CHECK ----------
    const exists = users.find(u => u.regno === regno || u.email === email);
    if(exists){
        error.innerText = "User already registered!";
        return;
    }

    // ---------- SAVE USER ----------
    users.push({
        email: email,
        regno: regno,
        password: hash(password)
    });

    saveUsers(users);

    success.innerText = "Registration successful! Redirecting to login...";

    // ---------- REDIRECT ----------
    setTimeout(() => {
        window.location.href = "login.html";
    }, 1500);
});
