package pl.edu.uwr.pum.recipeapp.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import pl.edu.uwr.pum.recipeapp.R
import pl.edu.uwr.pum.recipeapp.databinding.FragmentRecipeEditBinding
import pl.edu.uwr.pum.recipeapp.model.relations.RecipeIngredientCrossRef
import pl.edu.uwr.pum.recipeapp.view.IngredientAdapter

class RecipeEditFragment : Fragment(R.layout.fragment_recipe_edit), IngredientAdapter.OnItemClickListener {

    private lateinit var binding: FragmentRecipeEditBinding
    private val args: RecipeEditFragmentArgs by navArgs()
    private val viewModel: RecipeEditViewModel by viewModels()


    @ExperimentalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel.recipe = args.recipe

        binding = FragmentRecipeEditBinding.inflate(inflater, container, false)

        val ingredientAdapter = IngredientAdapter(this)

        binding.apply {
            recipeTitleEditText.setText(viewModel.recipe.recipeName)
            recipeDescriptionEditText.setText(viewModel.recipe.recipeDescription)
            recipeFavoriteCheckBox.isChecked = viewModel.recipe.isFavorite
            val creationDate = viewModel.recipe.dateFormatted
            recipeCreationDateTextView.text = getString(R.string.creationDate, creationDate)

            recipeFavoriteCheckBox.jumpDrawablesToCurrentState()

            recipeTitleEditText.addTextChangedListener {
                viewModel.recipe.recipeName = it.toString()
            }

            recipeDescriptionEditText.addTextChangedListener {
                viewModel.recipe.recipeDescription = it.toString()
            }

            recipeFavoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.recipe.isFavorite = isChecked
            }

            fabRecipeSubmit.setOnClickListener {
                viewModel.onSaveClick()
            }

            fabAddIngredient.setOnClickListener {
                viewModel.onAddIngredientClick()
            }

        }

        setFragmentResultListener("add_request") {_, bundle ->
            val result = bundle.getInt("add_result")
            viewModel.onAddResult(result)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.editRecipeEvent.collect { event ->
                when (event) {
                    is RecipeEditViewModel.EditRecipeEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is RecipeEditViewModel.EditRecipeEvent.NavigateBackWithResult -> {
                        binding.recipeDescriptionEditText.clearFocus()
                        binding.recipeTitleEditText.clearFocus()
                        setFragmentResult(
                            "edit_request",
                            bundleOf("edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is RecipeEditViewModel.EditRecipeEvent.NavigateToAddIngredientDialog -> {
                        val action =
                            RecipeEditFragmentDirections
                                .actionRecipeEditFragmentToIngredientAddDialogFragment(viewModel.recipe)
                        findNavController().navigate(action)
                    }
                    is RecipeEditViewModel.EditRecipeEvent.ShowIngredientSavedMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onItemClick(recipeIngredientCrossRef: RecipeIngredientCrossRef) {
        viewModel.onIngredientClick(recipeIngredientCrossRef)
    }

}