package com.tasty.recipesapp.models

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class RecipeRepository {

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

    fun getRecipeDetailsFromJson(context: Context): List<RecipeModel> {
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

}