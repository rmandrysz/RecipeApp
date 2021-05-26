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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import pl.edu.uwr.pum.recipeapp.R
import pl.edu.uwr.pum.recipeapp.databinding.FragmentRecipeAddDialogBinding

class RecipeAddDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentRecipeAddDialogBinding
    private val viewModel: RecipeAddViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentRecipeAddDialogBinding.inflate(inflater, container, false)

        binding.apply {
            dialogSubmitButton.setOnClickListener {
                viewModel.onSaveClick(dialogRecipeTitleEditText.text.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addRecipeEvent.collect { event ->
                when (event) {
                    is RecipeAddViewModel.AddRecipeEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is RecipeAddViewModel.AddRecipeEvent.NavigateBackWithResult -> {
                        binding.dialogRecipeTitleEditText.clearFocus()
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