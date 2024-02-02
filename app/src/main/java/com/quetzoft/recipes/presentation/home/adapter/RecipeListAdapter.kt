package com.quetzoft.recipes.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.quetzoft.recipes.R
import com.quetzoft.recipes.domain.model.Recipe

class RecipeListAdapter(private val itemList: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Set the values to the views
        Glide.with(holder.itemView.context)
            .load(currentItem.image)
            .apply(RequestOptions()
                .placeholder(R.drawable.outline_broken_image)
                .error(R.drawable.outline_broken_image)
            )
            .into(holder.imageViewRecipeItem)
        holder.textViewRecipeNameItem.text = currentItem.title
    }

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewRecipeItem: ImageView = itemView.findViewById(R.id.imageViewRecipeItem)
        val textViewRecipeNameItem: TextView = itemView.findViewById(R.id.textViewRecipeNameItem)
    }
}