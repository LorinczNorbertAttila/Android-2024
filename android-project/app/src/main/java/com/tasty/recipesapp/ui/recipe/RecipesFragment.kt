package com.tasty.recipesapp.ui.recipe

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.FragmentRecipesBinding
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.ui.recipe.adapter.RecipesListAdapter
import com.tasty.recipesapp.ui.recipe.viewmodel.RecipeListViewModel


class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipesAdapter: RecipesListAdapter
    private lateinit var viewModel: RecipeListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: RecipeFragment created.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RecipeListViewModel::class.java]
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        context?.let {
            viewModel.fetchRecipesFromJson(it)
        }

        initRecycleView()

        viewModel.recipesList.observe(viewLifecycleOwner) {
            recipes ->
//            for (recipe in recipes){
//                Log.d("RecipeData", "Recipe id: ${recipe.id}")
//                Log.d("RecipeData", "Recipe name: ${recipe.name}")
//                Log.d("RecipeData", "Recipe description: ${recipe.description}")
//                Log.d("RecipeData", "Recipe description: ${recipe.keywords}")
//                Log.d("RecipeData", "Recipe thumbnail: ${recipe.thumbnailUrl}")
//                Log.d("RecipeData", "\n")
//            }

            recipesAdapter.setData(recipes)
        }

        return binding.root
    }

    private fun initRecycleView() {
        recipesAdapter = RecipesListAdapter(ArrayList(), requireContext(),
            onItemClickListener = {
                    recipe ->
                navigateToRecipeDetail(recipe)
            })
        binding.recycleView.adapter = recipesAdapter
        binding.recycleView.layoutManager = LinearLayoutManager(context)
    }

    private fun navigateToRecipeDetail(recipe: RecipeModel) {
        findNavController()
            .navigate(
                R.id.action_recipesFragment_to_recipeDetailFragment,
                bundleOf(BUNDLE_EXTRA_SELECTED_RECIPE_ID to recipe.id))
    }

    companion object {
        const val BUNDLE_EXTRA_SELECTED_RECIPE_ID = "recipeId"
    }

}