package com.tasty.recipesapp.ui.profile

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.FragmentProfileRecipeDetailBinding
import com.tasty.recipesapp.models.RecipeDatabase
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.models.RecipeRepository
import com.tasty.recipesapp.ui.profile.viewmodel.ProfileRecipeDetailViewModel
import com.tasty.recipesapp.ui.recipe.factory.RecipeDetailViewModelFactory
import kotlinx.coroutines.launch

class ProfileRecipeDetailFragment : Fragment() {

    private lateinit var binding: FragmentProfileRecipeDetailBinding
    private lateinit var viewModel: ProfileRecipeDetailViewModel
    private var currentVideoPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: RecipeDetailFragment created.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileRecipeDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val screenHeight = resources.displayMetrics.heightPixels
        val imageHeight = (screenHeight * 0.5).toInt()
        binding.frameLayout.layoutParams.height = imageHeight
        binding.frameLayout.requestLayout()

        val recipeId = arguments?.getInt(ProfileFragment.BUNDLE_EXTRA_SELECTED_RECIPE_ID)
        Log.d(TAG, "Selected recipe id: $recipeId")

        val database = RecipeDatabase.getDatabase(requireContext())
        val repository = RecipeRepository(database.recipeDao())
        repository.initialize(requireContext())

        val factory = RecipeDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProfileRecipeDetailViewModel::class.java]

        recipeId?.let { fetchFromDatabase(it) }

        binding.deleteButton.setOnClickListener {
            lifecycleScope.launch {
                if (recipeId != null) {
                    viewModel.deleteFromDB(recipeId.toLong())
                }
            }
            findNavController().navigate(R.id.action_profileRecipeDetailFragment_to_profileFragment)
        }

    }

    private fun fetchFromDatabase(recipeId: Int) {
        lifecycleScope.launch {
            viewModel.fetchRecipeDetailsFromDB(recipeId.toLong())
            viewModel.recipe.observe(viewLifecycleOwner) { dbRecipe ->
                if (dbRecipe != null) {
                    updateViews(dbRecipe)
                } else {
                    Log.e(TAG, "Recipe details not found in DB either")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateViews(recipeModel: RecipeModel){
        binding.recipeDetailTitleView.text = recipeModel.name
        binding.recipeDetailDescriptionView.text = recipeModel.description
        binding.recipeDetailServingsView.text = "Number of servings: ${recipeModel.numServings}"
        binding.recipeDetailCountryView.text = "Country of origin: ${recipeModel.country}"
        val componentsText = recipeModel.components
            .sortedBy { it.position }
            .joinToString("\n") { "${it.position}. ${it.rawText}" }
        val instructionsText = recipeModel.instructions
            .sortedBy { it.position }
            .joinToString("\n") { "${it.position}. ${it.displayText}" }
        binding.recipeDetailInstructionsView.text = instructionsText
        binding.recipeDetailComponentsView.text = componentsText
        Glide.with(this)
            .load(recipeModel.thumbnailUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding .recipeDetailImageView)

        if (recipeModel.originalVideoUrl.isNotEmpty()) {
            binding.recipeDetailVideoView.visibility = View.VISIBLE
            binding.recipeDetailVideoView.setVideoPath(recipeModel.originalVideoUrl)

            val mediaController = android.widget.MediaController(binding.recipeDetailVideoView.context)
            mediaController.setAnchorView(binding.recipeDetailVideoView)
            binding.recipeDetailVideoView.setMediaController(mediaController)

            binding.recipeDetailVideoView.setOnPreparedListener {
                it.isLooping = true
                binding.recipeDetailVideoView.seekTo(currentVideoPosition)
                binding.recipeDetailVideoView.start()
            }

            binding.recipeDetailVideoView.setOnErrorListener { _, _, _ ->
                Log.e(TAG, "Error playing video")
                true
            }
        } else {
            binding.recipeDetailVideoView.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        if (binding.recipeDetailVideoView.isPlaying) {
            currentVideoPosition = binding.recipeDetailVideoView.currentPosition
            binding.recipeDetailVideoView.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentVideoPosition > 0) {
            binding.recipeDetailVideoView.seekTo(currentVideoPosition)
            binding.recipeDetailVideoView.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recipeDetailVideoView.stopPlayback()
    }
}