package pl.edu.uwr.pum.recipeapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import pl.edu.uwr.pum.recipeapp.model.RecipeDAO
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef
import java.security.AccessControlContext

@Database(
    entities = [
        Recipe::class,
        Ingredient::class,
        RecipeIngredientCrossRef::class
    ],
    version = 1,
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDAO

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getInstance(context: Context): RecipeDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_db3"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}