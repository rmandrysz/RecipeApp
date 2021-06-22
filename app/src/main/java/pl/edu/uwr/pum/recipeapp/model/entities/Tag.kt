package pl.edu.uwr.pum.recipeapp.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val tagId: Int = 0,
    var tagName: String
) : Parcelable
