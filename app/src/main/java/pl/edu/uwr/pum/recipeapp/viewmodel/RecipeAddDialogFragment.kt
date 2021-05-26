package pl.edu.uwr.pum.recipeapp.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.uwr.pum.recipeapp.R
import pl.edu.uwr.pum.recipeapp.databinding.FragmentRecipeAddDialogBinding

class RecipeAddDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentRecipeAddDialogBinding
    private val viewModel: ViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentRecipeAddDialogBinding.inflate(inflater, container, false)

        return binding.root
    }
}