package pl.edu.uwr.pum.recipeapp.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe

data class RecipeWithIngredients(
    @Embedded
    val recipe: Recipe,

    @Relation(
        parentColumn = "recipeId",
        entityColumn = "ingredientName",
        associateBy = Junction(RecipeIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)
