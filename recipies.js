const { DataTypes } = require("sequelize");
const sequelize = require("../sequelize");

const Recipe = sequelize.define("Recipe", {
    id: { type: DataTypes.UUID, defaultValue: DataTypes.UUIDV4, primaryKey: true },
    title: { type: DataTypes.STRING, allowNull: false },
    description: { type: DataTypes.TEXT },
    macros: { type: DataTypes.STRING },
    ingredients: { type: DataTypes.TEXT },
    instructions: { type: DataTypes.TEXT },
    image: { type: DataTypes.STRING }
});

module.exports = Recipe;
