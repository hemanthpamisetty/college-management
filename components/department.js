//===============================
// Department Page JavaScript
// ===============================

// Prevent browser back navigation
history.pushState(null, null, location.href);

window.onpopstate = function () {
    window.location.href = "dashbord.html";
};

// Check login session
window.onload = function () {
    if (!localStorage.getItem("isLoggedIn")) {
        window.location.href = "login.html";
    } else {
        fetchDepartments();
    }
};

async function fetchDepartments() {
    try {
        const response = await fetch("http://localhost:8080/api/departments");
        if (response.ok) {
            const departments = await response.json();
            console.log("Departments loaded from backend:", departments);
            // Ready to be populated in the UI when needed
        } else {
            console.error("Failed to fetch departments. Status:", response.status);
        }
    } catch (err) {
        console.error("Network error. Backend might be down.", err);
    }
}


const slider = document.getElementById("slider");
const scrollAmount = 300;
let autoScrollInterval;

/* ===== Manual Scroll ===== */
function scrollLeft() {
    slider.scrollBy({
        left: -scrollAmount,
        behavior: "smooth"
    });
}

function scrollRight() {
    slider.scrollBy({
        left: scrollAmount,
        behavior: "smooth"
    });
}

/* ===== Auto Scroll ===== */
function startAutoScroll() {
    autoScrollInterval = setInterval(() => {
        // if end reached → go back to start
        if (slider.scrollLeft + slider.clientWidth >= slider.scrollWidth - 10) {
            slider.scrollTo({ left: 0, behavior: "smooth" });
        } else {
            slider.scrollBy({ left: scrollAmount, behavior: "smooth" });
        }
    }, 4000); // every 4 seconds
}

function stopAutoScroll() {
    clearInterval(autoScrollInterval);
}

/* ===== Pause on Hover ===== */
slider.addEventListener("mouseenter", stopAutoScroll);
slider.addEventListener("mouseleave", startAutoScroll);

/* ===== Start on Load ===== */
window.addEventListener("load", startAutoScroll);


// Optional: department click log (future use)
document.addEventListener("DOMContentLoaded", () => {
    const deptButtons = document.querySelectorAll(".dept-card a");

    deptButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            console.log("Department opened:", btn.getAttribute("href"));
        });
    });
});