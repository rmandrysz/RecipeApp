package pl.edu.uwr.pum.recipeapp.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import pl.edu.uwr.pum.recipeapp.R
import pl.edu.uwr.pum.recipeapp.databinding.FragmentIngredientAddDialogBinding

class IngredientAddDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentIngredientAddDialogBinding
    private val args: IngredientAddDialogFragmentArgs by navArgs()
    private val viewModel: IngredientAddViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIngredientAddDialogBinding.inflate(inflater, container, false)

        binding.apply {
            ingredientDialogSubmitButton.setOnClickListener {
                val name = ingredientDialogTitleEditText.text.toString()
                val amount = ingredientDialogAmountEditText.text.toString()
                val recipeId = args.recipe.recipeId
                viewModel.onSubmitClick(name, amount, recipeId)
            }

            ingredientDialogCancelButton.setOnClickListener {
                viewModel.onCancelClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addIngredientEvent.collect { event ->
                when (event) {
                    is IngredientAddViewModel.AddIngredientEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is IngredientAddViewModel.AddIngredientEvent.NavigateBackWithResult -> {
                        binding.ingredientDialogTitleEditText.clearFocus()
                        binding.ingredientDialogAmountEditText.clearFocus()
                        setFragmentResult(
                            "add_request",
                            bundleOf("add_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }

        return binding.root
    }
}