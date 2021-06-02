package pl.edu.uwr.pum.recipeapp.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.edu.uwr.pum.recipeapp.EDIT_RECIPE_RESULT_CANCELLED
import pl.edu.uwr.pum.recipeapp.EDIT_RECIPE_RESULT_OK
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef
import pl.edu.uwr.pum.recipeapp.repository.Repository

class RecipeEditViewModel(@NonNull application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    lateinit var recipe: Recipe

    private val editRecipeEventChannel = Channel<EditRecipeEvent>()
    val editRecipeEvent = editRecipeEventChannel.receiveAsFlow()

    // TODO Implement recyclerview adapter observing associated ingredients
//    private val allIngredients: LiveData<List<Ingredient>>

    init {
        val dao = RecipeDatabase.getInstance(application).recipeDao()

        repository = Repository.getInstance(application, dao)

//        allIngredients.observe(repository.get)
    }

    fun onSaveClick() {
        if (recipe.recipeName.isBlank()) {
            showInvalidInputMessage("Title cannot be empty")
        } else {
            updateRecipe()
        }
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        editRecipeEventChannel.send(EditRecipeEvent.ShowInvalidInputMessage(msg))
    }

    private fun updateRecipe() = viewModelScope.launch {
        repository.updateRecipe(recipe)
        editRecipeEventChannel.send(EditRecipeEvent.NavigateBackWithResult(EDIT_RECIPE_RESULT_OK))
    }

    fun onIngredientClick(crossRef: RecipeIngredientCrossRef) {

    }

    fun onAddIngredientClick() = viewModelScope.launch {
        editRecipeEventChannel.send(EditRecipeEvent.NavigateToAddIngredientDialog)
    }

    fun onAddResult(result: Int) {
        when (result) {
            EDIT_RECIPE_RESULT_OK -> showIngredientSavedMessage("Recipe Saved")
            EDIT_RECIPE_RESULT_CANCELLED -> showIngredientSavedMessage("Operation Cancelled")
        }
    }

    private fun showIngredientSavedMessage(msg: String) = viewModelScope.launch {
        editRecipeEventChannel.send(EditRecipeEvent.ShowIngredientSavedMessage(msg))
    }

    sealed class EditRecipeEvent {
        data class ShowInvalidInputMessage(val msg: String) : EditRecipeEvent()
        data class NavigateBackWithResult(val result: Int) : EditRecipeEvent()
        object NavigateToAddIngredientDialog : EditRecipeEvent()
        data class ShowIngredientSavedMessage(val msg: String) : EditRecipeEvent()
    }
}