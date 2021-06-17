package pl.edu.uwr.pum.recipeapp.model.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import pl.edu.uwr.pum.recipeapp.model.entities.Ingredient
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe

@Parcelize
@Entity
data class RecipeIngredientCrossRef(
    @PrimaryKey(autoGenerate = true)
    val crossRefId: Int = 0,
    val recipeId: Int,
    var ingredientName: String,
    var ingredientAmount: String,
) : Parcelable
