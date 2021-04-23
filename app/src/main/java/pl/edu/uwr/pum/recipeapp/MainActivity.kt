package pl.edu.uwr.pum.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.model.RecipeDAO
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dao: RecipeDAO = RecipeDatabase.getInstance(this).recipeDao

        val recipes = listOf(
            Recipe(0, "Recipe1", "asdwasdwa"),
            Recipe(0, "Recipe2", "asdwasdwa"),
            Recipe(0, "Recipe3", "asdwasdwa"),
            Recipe(0, "Recipe4", "asdwasdwa"),
        )

        val ingredients = listOf(
            Ingredient("Ingredient1"),
            Ingredient("Ingredient2"),
            Ingredient("Ingredient3"),
            Ingredient("Ingredient4"),
        )

        val relations = listOf(
            RecipeIngredientCrossRef(1, "Ingredient1", "400g"),
            RecipeIngredientCrossRef(1, "Ingredient2", "800g"),
            RecipeIngredientCrossRef(2, "Ingredient1", "2 Tablespoons"),
            RecipeIngredientCrossRef(3, "Ingredient3", "1 Teaspoon"),
            RecipeIngredientCrossRef(3, "Ingredient2", "3 Cloves"),
        )

        lifecycleScope.launch {
            recipes.forEach { dao.insertRecipe(it) }
            ingredients.forEach { dao.insertIngredient(it) }
            relations.forEach { dao.insertRecipeIngredientCrossRef(it) }
        }
    }
}