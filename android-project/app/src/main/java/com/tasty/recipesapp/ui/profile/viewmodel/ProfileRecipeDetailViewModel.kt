package com.tasty.recipesapp.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.models.RecipeRepository

class ProfileRecipeDetailViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipe = MutableLiveData<RecipeModel?>()
    val recipe: LiveData<RecipeModel?> get() = _recipe

    suspend fun fetchRecipeDetailsFromDB(recipeId: Long) {
        val recipeDetails = repository.getRecipeByIdfromDB(recipeId)
        _recipe.postValue(recipeDetails)
    }

    suspend fun deleteFromDB(recipeId: Long){
        val recipeEntity = repository.getRecipefromDB(recipeId)
        if (recipeEntity != null) {
            repository.deleteRecipe(recipeEntity)
        }
    }
}
