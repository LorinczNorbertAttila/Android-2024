package com.tasty.recipesapp.ui.profile

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
import com.tasty.recipesapp.databinding.FragmentProfileBinding
import com.tasty.recipesapp.models.RecipeDatabase
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.models.RecipeRepository
import com.tasty.recipesapp.ui.profile.factory.ProfileViewModelFactory
import com.tasty.recipesapp.ui.profile.viewmodel.ProfileViewModel
import com.tasty.recipesapp.ui.recipe.adapter.RecipesListAdapter

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var recipesAdapter: RecipesListAdapter
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ProfileFragment created.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = RecipeRepository(RecipeDatabase.getDatabase(requireContext()).recipeDao())
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(repository))[ProfileViewModel::class.java]
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        initRecycleView()

        viewModel.getAllRecipes()
        viewModel.recipesList.observe(viewLifecycleOwner) { recipes ->
            recipesAdapter.setData(recipes)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_newRecipeFragment2)
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
                R.id.action_profileFragment_to_profileRecipeDetailFragment,
                bundleOf("recipeId" to recipe.id))
    }
    companion object {
        const val BUNDLE_EXTRA_SELECTED_RECIPE_ID = "recipeId"
    }
}