// ---------------- DEMO USER DATABASE ----------------
// (Later connect to backend DB for real security)
const users = [
    {
        regno: "24091A05AG",
        password: hash("123456")
    },
    {
        regno: "24091a05ag",
        password: hash("987654")
    }
];

// ---------------- HASH FUNCTION ----------------
function hash(text){
    let hash = 0;
    for(let i = 0; i < text.length; i++){
        hash = ((hash << 5) - hash) + text.charCodeAt(i);
        hash |= 0;
    }
    return hash.toString();
}

// ---------------- LOGIN LOGIC ----------------
document.getElementById("loginForm").addEventListener("submit", function(e){
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

    // -------- AUTHENTICATION --------
    const hashedPass = hash(password);

    const user = users.find(u => u.regno === regno && u.password === hashedPass);

    if(user){
        // -------- SESSION --------
        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("regno", regno);

        // -------- REDIRECT --------
        window.location.href = "dashbord.html";
    }else{
        error.innerText = "Invalid Registration Number or Password!";
    }
});
