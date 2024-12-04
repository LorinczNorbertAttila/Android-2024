package com.tasty.recipesapp.ui.recipe.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.RecipeListItemBinding
import com.tasty.recipesapp.models.RecipeModel

class RecipesListAdapter (
    private var recipesList: List<RecipeModel>,
    private val context: Context,
    private val onItemClickListener: (RecipeModel) -> Unit) :
    RecyclerView.Adapter<RecipesListAdapter.RecipeItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipesListAdapter.RecipeItemViewHolder {
        val binding = RecipeListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecipeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeItemViewHolder, position: Int) {
        val recipe = recipesList[position]
        holder.recipeTitleView.text = recipe.name
        holder.recipeDescriptionView.text = recipe.description
        holder.recipeKeywordView.text = recipe.keywords

        Glide.with(context)
            .load(recipe.thumbnailUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.recipeImageView)

        holder.blurView.setupWith(holder.root).setBlurRadius(10f)
    }

    inner class RecipeItemViewHolder(binding: RecipeListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //view referenciak az adapterbol
        val recipeTitleView: TextView = binding.recipeTitleView
        val recipeDescriptionView: TextView = binding.recipeDescriptionView
        val recipeKeywordView: TextView = binding.recipeKeywordsView
        val recipeImageView: ImageView = binding.recipeImageView
        val blurView: eightbitlab.com.blurview.BlurView = binding.blurView
        val root = binding.root

        init {
            binding.root.setOnClickListener {
                val currentPosition = this.adapterPosition
                val currentRecipe = recipesList[currentPosition]
                onItemClickListener(currentRecipe)
            }
        }
    }

    override fun getItemCount() = recipesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newRecipes: List<RecipeModel>) {
        recipesList = newRecipes
        notifyDataSetChanged()
    }
}