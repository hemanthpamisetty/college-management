// ================= SESSION PROTECTION =================
(function sessionCheck(){
    if(
        window.location.pathname.includes("dashbord") ||
        window.location.pathname.includes("services") ||
        window.location.pathname.includes("departments") ||
        window.location.pathname.includes("updates") ||
        window.location.pathname.includes("clginformation")
    ){
        if(localStorage.getItem("isLoggedIn") !== "true"){
            window.location.href = "login.html";
        }
    }
})();

// ================= BACK BUTTON CONTROL =================
history.pushState(null, null, location.href);
window.onpopstate = function () {
    // secure back navigation
    if(localStorage.getItem("isLoggedIn") === "true"){
        // if logged in → dashboard
        window.location.href = "dashbord.html";
    }else{
        // if not logged in → login
        window.location.href = "login.html";
    }
};

// ================= LOGOUT FUNCTION =================
function logout(){
    localStorage.clear();
    window.location.href = "login.html";
}

// ================= PAGE TRACKING =================
localStorage.setItem("lastPage", window.location.pathname);

