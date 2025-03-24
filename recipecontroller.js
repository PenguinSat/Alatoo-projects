const Recipe = require("../models/Recipe");

exports.getRecipes = async (req, res) => {
    const recipes = await Recipe.findAll();
    res.json(recipes);
};

exports.createRecipe = async (req, res) => {
    try {
        const { title, description, macros, ingredients, instructions } = req.body;
        const image = req.file ? `/uploads/${req.file.filename}` : null;
        const recipe = await Recipe.create({ title, description, macros, ingredients, instructions, image });

        res.status(201).json(recipe);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
