package pl.edu.uwr.pum.recipeapp.repository

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.model.RecipeDAO
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef
import pl.edu.uwr.pum.recipeapp.viewmodel.SortOrder

class Repository(private val dao: RecipeDAO) {


    fun getRecipes(searchQuery: String, sortOrder: SortOrder, showFavorite: Boolean) =
        dao.getRecipes(searchQuery, sortOrder, showFavorite)

    suspend fun deleteRecipe(recipe: Recipe)
    {
        dao.deleteRecipe(recipe)
        dao.deleteCrossRef(recipe.recipeId)
    }

    suspend fun insertRecipe(recipe: Recipe)
    {
        dao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe)
    {
        dao.updateRecipe(recipe)
    }

    suspend fun createNewIngredient(ingredient: Ingredient, ref: RecipeIngredientCrossRef)
    {
        dao.insertIngredient(ingredient)
        dao.insertRecipeIngredientCrossRef(ref)
    }


    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(context: Context, recipeDao: RecipeDAO): Repository {
            synchronized(this) {
                return INSTANCE ?: Repository(recipeDao).also {
                    INSTANCE = it
                }
            }
        }

    }
}