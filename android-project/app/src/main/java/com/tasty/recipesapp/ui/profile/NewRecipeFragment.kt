package com.tasty.recipesapp.ui.profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.FragmentNewRecipeBinding
import com.tasty.recipesapp.models.RecipeDatabase
import com.tasty.recipesapp.models.RecipeEntity
import com.tasty.recipesapp.models.RecipeRepository
import com.tasty.recipesapp.ui.profile.factory.ProfileViewModelFactory
import com.tasty.recipesapp.ui.profile.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class NewRecipeFragment : Fragment() {

    private lateinit var binding: FragmentNewRecipeBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewRecipeBinding.inflate(inflater, container, false)

        val recipeDao = RecipeDatabase.getDatabase(requireContext()).recipeDao()
        val repository = RecipeRepository(recipeDao)
        val viewModelFactory = ProfileViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        // "Save recipe" gomb kezelése
        binding.button.setOnClickListener {
            saveRecipe()
        }

        return binding.root
    }

    private fun saveRecipe() {
        val title = binding.titleEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val keywords = binding.keywordsEditText.text.toString()
        val thumbnailUrl = binding.thumbnailEditText.text.toString()
        val originalVideoUrl = binding.videoURLEditText.text.toString()
        val country = binding.countryEditText.text.toString()
        val numServings = binding.numServingsEditText.text.toString()
        val componentsText = binding.componentsEditText.text.toString()
        val instructionsText = binding.instructionsEditText.text.toString()

        // Ellenőrzés
        if (title.isBlank() || description.isBlank() || keywords.isBlank() || thumbnailUrl.isBlank() || country.isBlank() ||
            originalVideoUrl.isBlank() || numServings.isBlank() || componentsText.isBlank() || instructionsText.isBlank()) {
            Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        val components = componentsText.lines()
            .filter { it.isNotBlank() }
            .mapIndexed { index, componentText ->
                JSONObject().apply {
                    put("rawText", componentText)
                    put("extraComment", "no") // Ha szükséges
                    put("ingredient", JSONObject().apply {
                        put("name", "unknown")
                    })
                    put("measurement", JSONObject().apply {
                        put("quantity", "unknown")
                        put("unit", JSONObject().apply {
                            put("name", "unknown")
                            put("displaySingular", "unknown")
                            put("displayPlural", "unknown")
                            put("abbreviation", "unknown")
                        })
                    })
                    put("position", index + 1)
                }
            }

        // Instrukciók feldolgozása
        val instructions = instructionsText.lines()
            .filter { it.isNotBlank() }
            .mapIndexed { index, instructionText ->
                JSONObject().apply {
                    put("instructionID", index + 1)
                    put("position", index + 1)
                    put("displayText", instructionText)
                }
            }

        lifecycleScope.launch {
            try {
                // JSON felépítése
                val recipeJson = JSONObject().apply {
                    put("recipeID", 0)
                    put("name", title)
                    put("description", description)
                    put("thumbnailUrl", thumbnailUrl)
                    put("keywords", keywords)
                    put("isPublic", false)
                    put("userEmail", "Unknown")
                    put("originalVideoUrl", originalVideoUrl)
                    put("country", country)
                    put("numServings", numServings.toInt())
                    put("components", JSONArray(components))
                    put("instructions", JSONArray(instructions))
                }

                val recipeId = viewModel.insertRecipe(
                    RecipeEntity(json = recipeJson.toString())
                )

                // JSON frissítése a generált ID-val
                recipeJson.put("recipeID", recipeId)

                // Adatbázis frissítése az új JSON-nel
                viewModel.updateRecipe(
                    RecipeEntity(
                        internalId = recipeId.toLong(),
                        json = recipeJson.toString()
                    )
                )


                Toast.makeText(requireContext(), "Recipe saved successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_newRecipeFragment_to_profileFragment)
                clearFields()
            } catch (e: Exception) {
                Log.e(TAG, "Error saving recipe: ${e.message}")
                Toast.makeText(requireContext(), "Failed to save recipe", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun clearFields() {
        binding.titleEditText.text?.clear()
        binding.descriptionEditText.text?.clear()
        binding.keywordsEditText.text?.clear()
        binding.thumbnailEditText.text?.clear()
    }
}
