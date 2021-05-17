package pl.edu.uwr.pum.recipeapp.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.repository.Repository

class ViewModel(@NonNull application: Application) : AndroidViewModel(application){

    private val repository: Repository

    init {
        val dao = RecipeDatabase.getInstance(application).recipeDao()
        repository = Repository(dao)
    }

    val recipes = repository.getRecipes()
}