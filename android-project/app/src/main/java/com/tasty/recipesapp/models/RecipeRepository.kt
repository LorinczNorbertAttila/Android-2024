package com.tasty.recipesapp.models

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.IOException



class RecipeRepository(private val recipeDao: RecipeDao) {
    private var recipesList: List<RecipeModel> = ArrayList()
    private var recipeDetailsList: List<RecipeModel> = ArrayList()

    fun getRecipesFromJson(context: Context): List<RecipeModel> {
        lateinit var jsonString: String
        try {
            jsonString =
                context.assets.open("more_recipes.json")
                    .bufferedReader()
                    .use { it.readText() }
        } catch (ioException: IOException) {
            Log.e(TAG, "Error occured while reading JSON file: $ioException")
        }

        val type = object : TypeToken<List<RecipeDTO>>() {}.type
        val recipeDTOList: List<RecipeDTO> = Gson().fromJson(jsonString, type)

        recipesList = recipeDTOList.toModelList()

        return recipesList
    }

    private fun getRecipeDetailsFromJson(context: Context): List<RecipeModel> {
        lateinit var jsonString: String
        try {
            jsonString =
                context.assets.open("recipe_details.json")
                    .bufferedReader()
                    .use { it.readText() }
        } catch (ioException: IOException) {
            Log.e(TAG, "Error occurred while reading JSON file: $ioException")
        }

        val type = object : TypeToken<List<RecipeDTO>>() {}.type
        val recipeDTOList: List<RecipeDTO> = Gson().fromJson(jsonString, type)

        recipeDetailsList = recipeDTOList.toModelList()

        return recipeDetailsList
    }

    fun initialize(context: Context) {
        recipesList = getRecipesFromJson(context)
        recipeDetailsList = getRecipeDetailsFromJson(context)
    }

    fun getRecipeById(recipeId: Int): RecipeModel? {
        return recipeDetailsList.find { it.id == recipeId }
    }

    suspend fun insertRecipe(recipe: RecipeEntity) : Long {
        return recipeDao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: RecipeEntity) {
        recipeDao.updateRecipeJson(recipe.internalId, recipe.json)
    }

    suspend fun getAllRecipes(): List<RecipeModel> {
        return recipeDao.getAllRecipes().mapNotNull { recipeEntity ->
            try {
                // JSON feldolgozása
                val jsonObject = JSONObject(recipeEntity.json)
                jsonObject.put("id", recipeEntity.internalId) // Adding internalId to JSON
                Gson().fromJson(jsonObject.toString(), RecipeDTO::class.java).toModel()
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing recipe JSON: ${e.message}")
                null // Hibás JSON-t kihagyunk
            }
        }
    }

    suspend fun deleteRecipe(recipe: RecipeEntity) {
        recipeDao.deleteRecipe(recipe)
    }

    suspend fun getRecipefromDB(id: Long) :RecipeEntity? {
        return recipeDao.getRecipeById(id)
    }

    suspend fun getRecipeByIdfromDB(id: Long) : RecipeModel? {
        val recipeEntity = recipeDao.getRecipeById(id)
        return recipeEntity?.let { entity ->
            try {
                val recipeDto = Gson().fromJson(entity.json, RecipeDTO::class.java)
                recipeDto.toModel()
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing recipe JSON: ${e.message}")
                null
            }
        }
    }

}

