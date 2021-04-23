package pl.edu.uwr.pum.recipeapp.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe

data class IngredientWithRecipes(
    @Embedded
    val ingredient: Ingredient,

    @Relation(
        parentColumn = "ingredientName",
        entityColumn = "recipeId",
        associateBy = Junction(RecipeIngredientCrossRef::class)
    )
    val recipes: List<Recipe>
)
