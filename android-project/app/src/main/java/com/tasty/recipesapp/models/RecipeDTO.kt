package com.tasty.recipesapp.models

data class RecipeDTO(
    val recipeId: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val keywords: String,
    val isPublic: Boolean,
    val userEmail: String,
    val originalVideoUrl: String,
    val country: String,
    val numServings: Int,
//    val components: List<Any?>,
//    val instructions: List<Any?>,
)

data class RecipeModel(
    val name: String,
    val description: String,
    val thumbnailUrl: String,
)

fun RecipeDTO.toModel():   RecipeModel {
    return RecipeModel(
        name = this.name,
        description = this.description,
        thumbnailUrl = this.thumbnailUrl,
    )
}

fun List<RecipeDTO>.toModelList(): List<RecipeModel>
{ return this.map { it.toModel() } }
