package pl.edu.uwr.pum.recipeapp.model

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.model.relations.IngredientWithRecipes
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeWithIngredients
import pl.edu.uwr.pum.recipeapp.viewmodel.SortOrder

@Dao
interface RecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredientCrossRef(crossRef: RecipeIngredientCrossRef)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("DELETE FROM recipeingredientcrossref WHERE recipeId = :recipeId")
    suspend fun deleteCrossRef(recipeId: Int)

    @Transaction
    @Query("SELECT * FROM recipe WHERE recipeId = :recipeId")
    fun getRecipeWithIngredients(recipeId: Int): LiveData<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM ingredient WHERE ingredientName = :ingredientName")
    fun getIngredientWithRecipes(ingredientName: String): LiveData<List<IngredientWithRecipes>>

    @Query("SELECT * FROM recipe WHERE (isFavorite = :showFavorite OR isFavorite = 1) AND recipeName LIKE '%' || :searchedName || '%' ORDER BY recipeName")
    fun getRecipesByName(searchedName: String, showFavorite: Boolean): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE (isFavorite = :showFavorite OR isFavorite = 1) AND recipeName LIKE '%' || :searchedName || '%' ORDER BY date")
    fun getRecipesByDate(searchedName: String, showFavorite: Boolean): Flow<List<Recipe>>

    @Query("SELECT * FROM recipeingredientcrossref WHERE recipeId = :recipeId")
    fun getAssociatedReferences(recipeId: Int): Flow<List<RecipeIngredientCrossRef>>


    fun getRecipes(query: String, sortOrder: SortOrder, showFavorite: Boolean): Flow<List<Recipe>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getRecipesByName(query, showFavorite)
            SortOrder.BY_DATE -> getRecipesByDate(query, showFavorite)
        }

}