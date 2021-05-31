package pl.edu.uwr.pum.recipeapp.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import pl.edu.uwr.pum.recipeapp.R
import pl.edu.uwr.pum.recipeapp.databinding.FragmentIngredientAddDialogBinding

class IngredientAddDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentIngredientAddDialogBinding
    private val viewModel: IngredientAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIngredientAddDialogBinding.inflate(inflater, container, false)

        binding.apply {

        }

        return binding.root
    }
}