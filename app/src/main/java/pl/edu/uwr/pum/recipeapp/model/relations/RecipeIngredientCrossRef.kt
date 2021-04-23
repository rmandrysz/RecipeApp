package pl.edu.uwr.pum.recipeapp.model.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe

@Entity(primaryKeys = ["recipeId", "ingredientName"])
data class RecipeIngredientCrossRef(
    val recipeId: Int,
    val ingredientName: String,
    val ingredientAmount: String,
)
