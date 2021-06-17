package pl.edu.uwr.pum.recipeapp.repository

import android.content.Context
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
        dao.deleteAssociatedCrossReferences(recipe.recipeId)
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

    fun getAssociatedIngredients(recipeId: Int) =
        dao.getAssociatedReferences(recipeId)

    suspend fun getIngredient(ingredientName: String) =
        dao.getReferencedIngredient(ingredientName)

    suspend fun deleteCrossRef(crossRef: RecipeIngredientCrossRef) =
        dao.deleteCrossRef(crossRef)

    suspend fun insertCrossRef(crossRef: RecipeIngredientCrossRef) =
        dao.insertRecipeIngredientCrossRef(crossRef)

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