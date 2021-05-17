package pl.edu.uwr.pum.recipeapp.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.uwr.pum.recipeapp.R
import pl.edu.uwr.pum.recipeapp.databinding.FragmentRecipeListBinding
import pl.edu.uwr.pum.recipeapp.view.RecipeAdapter

class RecipeListFragment : Fragment() {

    private lateinit var binding: FragmentRecipeListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentRecipeListBinding.inflate(inflater, container, false)

        val recipeAdapter = RecipeAdapter()

        binding.apply {
            recipeRecyclerView.apply {
                adapter = recipeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        return binding.root
    }

}