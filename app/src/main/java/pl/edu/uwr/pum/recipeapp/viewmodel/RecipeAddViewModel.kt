package pl.edu.uwr.pum.recipeapp.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.edu.uwr.pum.recipeapp.ADD_RECIPE_RESULT_OK
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.repository.Repository

class RecipeAddViewModel(@NonNull application: Application) : AndroidViewModel(application) {

    private val repository: Repository

    private val addRecipeEventChannel = Channel<AddRecipeEvent>()
    val addRecipeEvent = addRecipeEventChannel.receiveAsFlow()

    init {
        val dao = RecipeDatabase.getInstance(application).recipeDao()

        repository = Repository(dao)
    }

    fun onSaveClick(title: String) {
        if (title.isBlank()) {
            showInvalidInputMessage("Title cannot be empty")
        } else {
            val newRecipe = Recipe(recipeName = title)

            createNewRecipe(newRecipe)
        }
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        addRecipeEventChannel.send(AddRecipeEvent.ShowInvalidInputMessage(msg))
    }

    private fun createNewRecipe(recipe: Recipe) = viewModelScope.launch {
        repository.insertRecipe(recipe)
        addRecipeEventChannel.send(AddRecipeEvent.NavigateBackWithResult(ADD_RECIPE_RESULT_OK))
    }

    sealed class AddRecipeEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddRecipeEvent()
        data class NavigateBackWithResult(val result: Int) : AddRecipeEvent()
    }
}