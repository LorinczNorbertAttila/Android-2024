package com.tasty.recipesapp.models

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.annotations.SerializedName

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
    val measurement: MeasurementDTO,
    val position: Int
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
    val originalVideoUrl: String,
    val country: String,
    val numServings: Int,
    val components: List<ComponentModel> = emptyList(),
    val instructions: List<InstructionModel> = emptyList()
)

data class ComponentModel(
    val rawText: String,
    val position: Int
)

data class InstructionModel(
    val displayText: String,
    val position: Int
)

fun ComponentDTO.toModel(): ComponentModel {
    return ComponentModel(
        rawText = this.rawText,
        position = this.position
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
        originalVideoUrl = this.originalVideoUrl,
        country = this.country,
        numServings = this.numServings,
        components = this.components?.map { it.toModel() } ?: emptyList(),
        instructions = this.instructions?.map { it.toModel() } ?: emptyList()
    )
}

fun List<RecipeDTO>.toModelList(): List<RecipeModel>
{ return this.map { it.toModel() } }

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val internalId: Long = 0L, // Room will handle generating this ID
    @SerializedName("json_data")
    val json: String
)

@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity) : Long
    @Query("SELECT * FROM recipe WHERE internalId = :id")
    suspend fun getRecipeById(id: Long): RecipeEntity?
    @Query("UPDATE recipe SET json = :json WHERE internalId = :id")
    suspend fun updateRecipeJson(id: Long, json: String)
    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<RecipeEntity>
    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}

@Database(entities = [RecipeEntity::class], version = 1, exportSchema =
false)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    companion object {
        // Create a singleton instance of the database
        @Volatile
        private var INSTANCE: RecipeDatabase? = null
        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


