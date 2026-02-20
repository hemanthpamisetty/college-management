// Toggle account menu
const accountBtn = document.getElementById("account");
const accountMenu = document.getElementById("accounty");

accountBtn.addEventListener("click", (e) => {
    e.preventDefault();
    accountMenu.style.display =
        accountMenu.style.display === "block" ? "none" : "block";
});

// Close dropdown when clicking outside
document.addEventListener("click", (e) => {
    if (!e.target.closest(".account")) {
        accountMenu.style.display = "none";
    }
});

// Logout
document.getElementById("logoutBtn").addEventListener("click", () => {
    localStorage.clear();          // clear session
    window.location.href = "login.html";
});
