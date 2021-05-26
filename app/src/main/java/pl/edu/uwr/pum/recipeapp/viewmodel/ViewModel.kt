package pl.edu.uwr.pum.recipeapp.viewmodel

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.edu.uwr.pum.recipeapp.database.RecipeDatabase
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.repository.Repository

class ViewModel(@NonNull application: Application) : AndroidViewModel(application) {

    val searchQuery = MutableStateFlow("")
    private val repository: Repository
    val sortOrder = MutableStateFlow(SortOrder.BY_NAME)
    val showFavorite = MutableStateFlow(false)

    private val recipeEventChannel = Channel<RecipeEvent>()
    val recipeEvent = recipeEventChannel.receiveAsFlow()

    init {
        val dao = RecipeDatabase.getInstance(application).recipeDao()
        repository = Repository(dao)
    }

    @ExperimentalCoroutinesApi
    private val recipeFlow = combine(
        searchQuery,
        sortOrder,
        showFavorite
    ) {
        query, sortOrder, showFavorite ->
        Triple(query, sortOrder, showFavorite)
    }.flatMapLatest { (query, sortOrder, showFavorite) ->
        repository.getRecipes(query, sortOrder, showFavorite)
    }

    @ExperimentalCoroutinesApi
    val allRecipes = recipeFlow.asLiveData()

    fun onRecipeSwiped(recipe: Recipe) = viewModelScope.launch {
        repository.deleteRecipe(recipe)
        recipeEventChannel.send(RecipeEvent.ShowUndoDeleteRecipeMessage(recipe))
    }

    fun onUndoDeleteRecipeClick(recipe: Recipe) = viewModelScope.launch {
        repository.insertRecipe(recipe)
    }

    fun onAddNewRecipeClick() = viewModelScope.launch {
        recipeEventChannel.send(RecipeEvent.NavigateToAddRecipeDialog)
    }

    fun onRecipeSelected(recipe: Recipe) = viewModelScope.launch {
        recipeEventChannel.send(RecipeEvent.NavigateToEditRecipeDialog(recipe))
    }

    sealed class RecipeEvent {
        data class ShowUndoDeleteRecipeMessage(val recipe: Recipe) : RecipeEvent()
        object NavigateToAddRecipeDialog : RecipeEvent()
        data class NavigateToEditRecipeDialog(val recipe: Recipe) : RecipeEvent()
    }
}

enum class SortOrder { BY_NAME, BY_DATE }