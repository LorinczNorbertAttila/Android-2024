package com.tasty.recipesapp.ui.recipe

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.FragmentRecipeDetailBinding
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.models.RecipeRepository
import com.tasty.recipesapp.ui.recipe.factory.RecipeDetailViewModelFactory
import com.tasty.recipesapp.ui.recipe.viewmodel.RecipeDetailViewModel

class RecipeDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: RecipeDetailFragment created.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt(RecipesFragment.BUNDLE_EXTRA_SELECTED_RECIPE_ID)
        Log.d(TAG, "Selected recipe id: $recipeId")

        val repository = RecipeRepository()
        repository.initialize(requireContext())

        val factory = RecipeDetailViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[RecipeDetailViewModel::class.java]

        recipeId?.let { viewModel.fetchRecipeDetails(it) }

        viewModel.recipe.observe(viewLifecycleOwner) {
            Log.d(TAG,"Selected recipe's details: $it")
            it?.let { updateViews(it) }
        }
    }


    private fun updateViews(recipeModel: RecipeModel){
        binding.recipeDetailTitleView.text = recipeModel.name
        binding.recipeDetailDescriptionView.text = recipeModel.description
        Glide.with(this)
            .load(recipeModel.thumbnailUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding .recipeDetailImageView)
    }
}