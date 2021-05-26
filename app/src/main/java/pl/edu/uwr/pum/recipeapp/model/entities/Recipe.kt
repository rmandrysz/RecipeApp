package pl.edu.uwr.pum.recipeapp.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.text.DateFormat
import java.util.*

@Parcelize
@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Int = 0,
    var recipeName: String,
    var recipeDescription: String = "",
    var isFavorite: Boolean = false,
    val date: Long = System.currentTimeMillis()
) : Parcelable {
    val dateFormatted : String
        get() = DateFormat.getDateTimeInstance().format(date)
}
