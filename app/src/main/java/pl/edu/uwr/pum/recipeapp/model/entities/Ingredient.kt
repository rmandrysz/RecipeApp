package pl.edu.uwr.pum.recipeapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Int = 0,
    var ingredientName: String,
)
