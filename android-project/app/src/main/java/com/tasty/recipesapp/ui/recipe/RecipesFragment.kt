package com.tasty.recipesapp.ui.recipe

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tasty.recipesapp.R
import com.tasty.recipesapp.ui.recipe.viewmodel.RecipeListViewModel


class RecipesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: RecipeFragment created.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = ViewModelProvider(this)[RecipeListViewModel::class.java]

        context?.let {
            viewModel.fetchRecipesFromJson(it)
        }
        viewModel.recipesList.observe(viewLifecycleOwner) {
            recipes ->
            for (recipe in recipes){
                Log.d("RecipeData", "Recipe name: ${recipe.name}")
                Log.d("RecipeData", "Recipe description: ${recipe.description}")
                Log.d("RecipeData", "Recipe thumbnail: ${recipe.thumbnailUrl}")
                Log.d("RecipeData", "\n")
            }
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

}