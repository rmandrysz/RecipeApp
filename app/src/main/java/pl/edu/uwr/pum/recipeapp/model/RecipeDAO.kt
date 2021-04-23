package pl.edu.uwr.pum.recipeapp.model

import androidx.room.*
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.model.relations.IngredientWithRecipes
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeWithIngredients

@Dao
interface RecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredientCrossRef(crossRef: RecipeIngredientCrossRef)

    @Transaction
    @Query("SELECT * FROM recipe WHERE recipeId = :recipeId")
    suspend fun getRecipeWithIngredients(recipeId: Int): List<RecipeWithIngredients>

    @Transaction
    @Query("SELECT * FROM ingredient WHERE ingredientName = :ingredientName")
    suspend fun getIngredientWithRecipes(ingredientName: String): List<IngredientWithRecipes>

}