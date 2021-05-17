package pl.edu.uwr.pum.recipeapp.repository

import android.content.Context
import androidx.room.Room
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.model.RecipeDAO

class Repository(private val dao: RecipeDAO) {

    fun getRecipes() = dao.getRecipes()

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