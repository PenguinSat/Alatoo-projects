const express = require("express");
const authController = require("../controllers/authController");
const recipeController = require("../controllers/recipeController");
const auth = require("../middleware/auth");
const multer = require("multer");

const router = express.Router();
const upload = multer({ dest: "uploads/" });

// Аутентификация
router.post("/register", authController.register);
router.post("/login", authController.login);

// Рецепты
router.get("/recipes", recipeController.getRecipes);
router.post("/recipes", auth, upload.single("image"), recipeController.createRecipe);

module.exports = router;
