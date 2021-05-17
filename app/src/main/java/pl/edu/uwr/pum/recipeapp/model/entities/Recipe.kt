package pl.edu.uwr.pum.recipeapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Int = 0,
    val recipeName: String,
    val recipeDescription: String,
    val isFavorite: Boolean = false,
)
