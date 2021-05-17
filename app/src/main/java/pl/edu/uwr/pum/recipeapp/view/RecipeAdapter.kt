package pl.edu.uwr.pum.recipeapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uwr.pum.recipeapp.databinding.RecipeCardBinding
import pl.edu.uwr.pum.recipeapp.model.entities.Recipe

class RecipeAdapter: ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(DiffCallback()) {

    class RecipeViewHolder(val binding: RecipeCardBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(recipe: Recipe) {
            binding.apply {
                titleTextView.text = recipe.recipeName
                favoriteIndicator.isVisible = recipe.isFavorite
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(RecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

    }


    class DiffCallback: DiffUtil.ItemCallback<Recipe>()
    {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem.recipeId == newItem.recipeId

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem == newItem
    }
}