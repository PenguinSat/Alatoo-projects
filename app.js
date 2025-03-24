const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");
const sequelize = require("./sequelize");
const routes = require("./routes/index");

const app = express();
app.use(cors());
app.use(bodyParser.json());
app.use("/uploads", express.static("uploads"));
app.use("/api", routes);

sequelize.sync().then(() => console.log("Database connected"));

app.listen(5000, () => console.log("Server running on port 5000"));
