package pl.edu.uwr.pum.recipeapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.edu.uwr.pum.recipeapp.ADD_RECIPE_RESULT_CANCELLED
import pl.edu.uwr.pum.recipeapp.ADD_RECIPE_RESULT_OK
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef
import pl.edu.uwr.pum.recipeapp.repository.Repository

class IngredientAddViewModel(@NonNull application: Application) :
    AndroidViewModel(application) {

    private val repository: Repository

    private val addIngredientEventChannel = Channel<AddIngredientEvent>()
    val addIngredientEvent = addIngredientEventChannel.receiveAsFlow()

    init {
        val dao = RecipeDatabase.getInstance(application).recipeDao()

        repository = Repository.getInstance(application, dao)
    }

    fun onSubmitClick(name: String, amount: String, recipeId: Int) {
        if (name.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        val newIngredient = Ingredient(name)
        val newRef = RecipeIngredientCrossRef(recipeId, name, amount)
        createNewIngredient(newIngredient, newRef)
    }

    private fun createNewIngredient(ingredient: Ingredient, ref: RecipeIngredientCrossRef) =
        viewModelScope.launch {
            repository.createNewIngredient(ingredient, ref)
            addIngredientEventChannel.send(
                AddIngredientEvent.NavigateBackWithResult(
                    ADD_RECIPE_RESULT_OK
                )
            )
        }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        addIngredientEventChannel.send(AddIngredientEvent.ShowInvalidInputMessage(msg))
    }

    fun onCancelClick() = viewModelScope.launch {
        addIngredientEventChannel.send(
            AddIngredientEvent.NavigateBackWithResult(
                ADD_RECIPE_RESULT_CANCELLED
            )
        )
    }

    sealed class AddIngredientEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddIngredientEvent()
        data class NavigateBackWithResult(val result: Int) : AddIngredientEvent()
    }
}