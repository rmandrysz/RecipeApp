package pl.edu.uwr.pum.recipeapp.model.relations

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = ["recipeId", "tagId"])
data class RecipeTagCrossRef(
    val recipeId: Int,
    val tagId: Int
) : Parcelable
