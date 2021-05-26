package pl.edu.uwr.pum.recipeapp.viewmodel

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import pl.edu.uwr.pum.recipeapp.R
import pl.edu.uwr.pum.recipeapp.databinding.FragmentRecipeListBinding
import pl.edu.uwr.pum.recipeapp.util.onQueryTextChanged
import pl.edu.uwr.pum.recipeapp.view.RecipeAdapter

class RecipeListFragment : Fragment() {

    private lateinit var binding: FragmentRecipeListBinding
    private val viewModel: ViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentRecipeListBinding.inflate(inflater, container, false)

        val recipeAdapter = RecipeAdapter()

        binding.apply {
            recipeRecyclerView.apply {
                adapter = recipeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val recipe = recipeAdapter.currentList[viewHolder.bindingAdapterPosition]
                    viewModel.onRecipeSwiped(recipe)
                }
            }).attachToRecyclerView(recipeRecyclerView)

            fabAddRecipe.setOnClickListener {
                viewModel.onAddNewRecipeClick()
            }
        }

        setFragmentResultListener("add_request") { _, bundle ->
            val result = bundle.getInt("add_result")
            viewModel.onAddResult(result)
        }

        viewModel.allRecipes.observe(viewLifecycleOwner) {
            recipeAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipeListEvent.collect { event ->
                when (event) {
                    is ViewModel.RecipeListEvent.ShowUndoDeleteRecipeListMessage -> {
                            Snackbar.make(requireView(), "Recipe deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO") {
                                    viewModel.onUndoDeleteRecipeClick(event.recipe)
                                }.show()
                    }
                    is ViewModel.RecipeListEvent.NavigateToAddRecipeListDialog -> {
                        val action = RecipeListFragmentDirections.actionRecipeListFragmentToRecipeAddDialogFragment()
                        findNavController().navigate(action)
                    }
                    is ViewModel.RecipeListEvent.NavigateToEditRecipeListDialog -> {

                    }
                    is ViewModel.RecipeListEvent.ShowRecipeSavedMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_recipe_list, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }

            R.id.menu_sort_by_date -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.menu_show_favorite -> {
                item.isChecked = !item.isChecked
                viewModel.showFavorite.value = item.isChecked
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}