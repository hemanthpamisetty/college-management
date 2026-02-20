const express = require("express");
const path = require("path");

const app = express();
const PORT = 3000;

// Serve all static files (HTML, CSS, JS, Images)
app.use(express.static(__dirname));

// Start from interface.html inside components
app.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, "components", "interface.html"));
});

app.listen(PORT, () => {
    console.log("Server started at http://localhost:3000");
});