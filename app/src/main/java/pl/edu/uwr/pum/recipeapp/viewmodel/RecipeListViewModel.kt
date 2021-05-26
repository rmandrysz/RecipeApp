package pl.edu.uwr.pum.recipeapp.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.edu.uwr.pum.recipeapp.ADD_RECIPE_RESULT_CANCELLED
import pl.edu.uwr.pum.recipeapp.ADD_RECIPE_RESULT_OK
import pl.edu.uwr.pum.recipeapp.EDIT_RECIPE_RESULT_CANCELLED
import pl.edu.uwr.pum.recipeapp.EDIT_RECIPE_RESULT_OK
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.repository.Repository

class ViewModel(@NonNull application: Application) : AndroidViewModel(application) {

    val searchQuery = MutableStateFlow("")
    private val repository: Repository
    val sortOrder = MutableStateFlow(SortOrder.BY_NAME)
    val showFavorite = MutableStateFlow(false)

    private val recipeListEventChannel = Channel<RecipeListEvent>()
    val recipeListEvent = recipeListEventChannel.receiveAsFlow()

    init {
        val dao = RecipeDatabase.getInstance(application).recipeDao()
        repository = Repository.getInstance(application, dao)
    }

    @ExperimentalCoroutinesApi
    private val recipeFlow = combine(
        searchQuery,
        sortOrder,
        showFavorite
    ) { query, sortOrder, showFavorite ->
        Triple(query, sortOrder, showFavorite)
    }.flatMapLatest { (query, sortOrder, showFavorite) ->
        repository.getRecipes(query, sortOrder, showFavorite)
    }

    @ExperimentalCoroutinesApi
    val allRecipes = recipeFlow.asLiveData()

    fun onRecipeSwiped(recipe: Recipe) = viewModelScope.launch {
        repository.deleteRecipe(recipe)
        recipeListEventChannel.send(RecipeListEvent.ShowUndoDeleteRecipeListMessage(recipe))
    }

    fun onUndoDeleteRecipeClick(recipe: Recipe) = viewModelScope.launch {
        repository.insertRecipe(recipe)
    }

    fun onAddNewRecipeClick() = viewModelScope.launch {
        recipeListEventChannel.send(RecipeListEvent.NavigateToAddRecipeListDialog)
    }

    fun onRecipeSelected(recipe: Recipe) = viewModelScope.launch {
        recipeListEventChannel.send(RecipeListEvent.NavigateToEditRecipeListDialog(recipe))
    }

    fun onAddResult(result: Int) {
        when (result) {
            ADD_RECIPE_RESULT_OK -> showRecipeSavedMessage("Recipe Saved")
            ADD_RECIPE_RESULT_CANCELLED -> showRecipeSavedMessage("Operation Cancelled")
        }
    }

    fun onEditResult(result: Int) {

        when (result) {
            EDIT_RECIPE_RESULT_OK -> showRecipeSavedMessage("Recipe Saved")
            EDIT_RECIPE_RESULT_CANCELLED -> showRecipeSavedMessage("Operation Cancelled")
        }


    }

    private fun showRecipeSavedMessage(msg: String) = viewModelScope.launch {
        recipeListEventChannel.send(RecipeListEvent.ShowRecipeSavedMessage(msg))
    }



    sealed class RecipeListEvent {
        data class ShowUndoDeleteRecipeListMessage(val recipe: Recipe) : RecipeListEvent()
        object NavigateToAddRecipeListDialog : RecipeListEvent()
        data class NavigateToEditRecipeListDialog(val recipe: Recipe) : RecipeListEvent()
        data class ShowRecipeSavedMessage(val msg: String) : RecipeListEvent()
    }
}

enum class SortOrder { BY_NAME, BY_DATE }