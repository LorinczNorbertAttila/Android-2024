package com.tasty.recipesapp.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.models.RecipeEntity
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.models.RecipeRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipesList = MutableLiveData<List<RecipeModel>>()
    val recipesList: LiveData<List<RecipeModel>> get() = _recipesList

    suspend fun insertRecipe(recipe: RecipeEntity): Int {
        return repository.insertRecipe(recipe).toInt()
    }

    suspend fun updateRecipe(recipe: RecipeEntity) {
        repository.updateRecipe(recipe)
    }

    fun getAllRecipes() {
        viewModelScope.launch {
            val recipes = repository.getAllRecipes()
            _recipesList.postValue(recipes)  // Update LiveData with the fetched recipes
        }
    }
}
