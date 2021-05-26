package pl.edu.uwr.pum.recipeapp.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.uwr.pum.recipeapp.R
import pl.edu.uwr.pum.recipeapp.databinding.FragmentRecipeAddBinding

class RecipeAddFragment : Fragment() {
    private lateinit var binding: FragmentRecipeAddBinding
    private val viewModel: ViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentRecipeAddBinding.inflate(inflater, container, false)

        return binding.root
    }
}