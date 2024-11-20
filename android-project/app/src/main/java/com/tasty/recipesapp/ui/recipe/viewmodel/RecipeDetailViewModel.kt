package com.tasty.recipesapp.ui.recipe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.models.RecipeRepository

class RecipeDetailViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipe = MutableLiveData<RecipeModel?>()
    val recipe: LiveData<RecipeModel?> get() = _recipe

    fun fetchRecipeDetails(recipeId: Int) {
        val recipeDetails = repository.getRecipeById(recipeId)
        _recipe.postValue(recipeDetails)
    }
}
