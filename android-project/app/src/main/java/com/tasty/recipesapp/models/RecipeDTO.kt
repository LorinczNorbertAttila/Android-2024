package com.tasty.recipesapp.models

data class RecipeDTO(
    val recipeID: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val keywords: String,
    val isPublic: Boolean,
    val userEmail: String,
    val originalVideoUrl: String,
    val country: String,
    val numServings: Int,
    val components: List<ComponentDTO>?,
    val instructions: List<InstructionDTO>?
)

data class ComponentDTO(
    val rawText: String,
    val extraComment: String,
    val ingredient: IngredientDTO,
    val measurement: MeasurementDTO
)

data class IngredientDTO(
    val name: String
)

data class MeasurementDTO(
    val quantity: String,
    val unit: UnitDTO
)

data class UnitDTO(
    val name: String,
    val displaySingular: String,
    val displayPlural: String,
    val abbreviation: String
)

data class InstructionDTO(
    val instructionID: Int,
    val displayText: String,
    val position: Int
)

data class RecipeModel(
    val id: Int,
    val name: String,
    val description: String,
    val keywords: String,
    val thumbnailUrl: String,
    val components: List<ComponentModel> = emptyList(),
    val instructions: List<InstructionModel> = emptyList()
)

data class ComponentModel(
    val rawText: String,
    val ingredientName: String,
    val quantity: String,
    val unit: String
)

data class InstructionModel(
    val displayText: String,
    val position: Int
)

fun ComponentDTO.toModel(): ComponentModel {
    return ComponentModel(
        rawText = this.rawText,
        ingredientName = this.ingredient.name,
        quantity = this.measurement.quantity,
        unit = this.measurement.unit.name
    )
}

fun InstructionDTO.toModel(): InstructionModel {
    return InstructionModel(
        displayText = this.displayText,
        position = this.position
    )
}

fun RecipeDTO.toModel(): RecipeModel {
    return RecipeModel(
        id = this.recipeID,
        name = this.name,
        description = this.description,
        keywords = this.keywords,
        thumbnailUrl = this.thumbnailUrl,
        components = this.components?.map { it.toModel() } ?: emptyList(),
        instructions = this.instructions?.map { it.toModel() } ?: emptyList()
    )
}

fun List<RecipeDTO>.toModelList(): List<RecipeModel>
{ return this.map { it.toModel() } }



