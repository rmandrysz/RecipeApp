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
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef
import pl.edu.uwr.pum.recipeapp.repository.Repository

class IngredientAddViewModel(@NonNull application: Application) :
    AndroidViewModel(application) {

    private val repository: Repository
    lateinit var crossRef: RecipeIngredientCrossRef
    lateinit var recipe: Recipe
    lateinit var ingredient: Ingredient

    private val addIngredientEventChannel = Channel<AddIngredientEvent>()
    val addIngredientEvent = addIngredientEventChannel.receiveAsFlow()

    init {
        val dao = RecipeDatabase.getInstance(application).recipeDao()

        repository = Repository.getInstance(application, dao)
    }

    fun onSubmitClick() {
        if (crossRef.ingredientName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        createNewIngredient()
    }

    private fun createNewIngredient() =
        viewModelScope.launch {

            ingredient.ingredientName = crossRef.ingredientName

            repository.createNewIngredient(ingredient, crossRef)
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

    fun initRef(ref: RecipeIngredientCrossRef?) {
        ref?.let {
            crossRef = ref
        } ?: kotlin.run {
            crossRef = RecipeIngredientCrossRef(
                recipeId = recipe.recipeId,
                ingredientName = "", ingredientAmount = ""
            )
        }

        initIngredient()
    }

    private fun initIngredient() = viewModelScope.launch {
        val ingredientList = repository.getIngredient(crossRef.ingredientName)

        ingredient = if (ingredientList.isEmpty()) {
            Ingredient(ingredientName = "")
        } else {
            ingredientList[0]
        }

    }

    fun initRecipe(recipe: Recipe) {
        this.recipe = recipe
    }

    sealed class AddIngredientEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddIngredientEvent()
        data class NavigateBackWithResult(val result: Int) : AddIngredientEvent()
    }
}