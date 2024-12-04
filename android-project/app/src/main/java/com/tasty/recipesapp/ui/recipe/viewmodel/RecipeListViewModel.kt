package com.tasty.recipesapp.ui.recipe.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.models.RecipeRepository

class RecipeListViewModel(private val repository: RecipeRepository) : ViewModel() {
    var recipesList : MutableLiveData<List<RecipeModel>> = MutableLiveData()


    fun fetchRecipesFromJson(context: Context) {
        val recipes = repository.getRecipesFromJson(context)
        recipesList.value = recipes
    }
}