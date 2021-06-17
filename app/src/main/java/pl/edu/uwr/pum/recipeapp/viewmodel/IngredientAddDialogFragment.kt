package pl.edu.uwr.pum.recipeapp.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
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
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef

class IngredientAddDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentIngredientAddDialogBinding
    private val args: IngredientAddDialogFragmentArgs by navArgs()
    private val viewModel: IngredientAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.initRecipe(args.recipe)
        viewModel.initRef(args.crossRef)

        binding = FragmentIngredientAddDialogBinding.inflate(inflater, container, false)

        binding.apply {
            ingredientDialogSubmitButton.setOnClickListener {
                viewModel.onSubmitClick()
            }

            ingredientDialogCancelButton.setOnClickListener {
                viewModel.onCancelClick()
            }

            ingredientDialogTitleEditText.addTextChangedListener {
                viewModel.crossRef.ingredientName = it.toString()
            }

            ingredientDialogAmountEditText.addTextChangedListener {
                viewModel.crossRef.ingredientAmount = it.toString()
            }

            ingredientDialogTitleEditText.setText(viewModel.crossRef.ingredientName)
            ingredientDialogAmountEditText.setText(viewModel.crossRef.ingredientAmount)
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