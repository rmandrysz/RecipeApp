package pl.edu.uwr.pum.recipeapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uwr.pum.recipeapp.databinding.IngredientCardBinding
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef

class IngredientAdapter(private val listener: OnItemClickListener)
    : ListAdapter<RecipeIngredientCrossRef, IngredientAdapter.RecipeIngredientCrossRefViewHolder>(IngredientAdapter.DiffCallback())
{
    inner class RecipeIngredientCrossRefViewHolder(val binding: IngredientCardBinding): RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val ingredientCrossRef = getItem(position)
                        listener.onItemClick(ingredientCrossRef)
                    }
                }
            }
        }

        fun bind(crossRef: RecipeIngredientCrossRef) {
            binding.apply {
                nameTextView.text = crossRef.ingredientName
                nameTextView.isSelected = true
                amountTextView.text = crossRef.ingredientAmount
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientCrossRefViewHolder {
        val binding = IngredientCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeIngredientCrossRefViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeIngredientCrossRefViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback: DiffUtil.ItemCallback<RecipeIngredientCrossRef>()
    {
        override fun areItemsTheSame(oldItem: RecipeIngredientCrossRef, newItem: RecipeIngredientCrossRef): Boolean =
            oldItem.ingredientName == newItem.ingredientName
                    && oldItem.recipeId == newItem.recipeId
                    && oldItem.ingredientAmount == newItem.ingredientAmount

        override fun areContentsTheSame(oldItem: RecipeIngredientCrossRef, newItem: RecipeIngredientCrossRef): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(recipeIngredientCrossRef: RecipeIngredientCrossRef)
    }

}