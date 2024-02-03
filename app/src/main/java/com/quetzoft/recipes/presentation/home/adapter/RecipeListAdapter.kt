package com.quetzoft.recipes.presentation.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.quetzoft.recipes.R
import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.presentation.recipe_detail.RecipeDetailActivity

class RecipeListAdapter(private val context: Context, private val itemList: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Set the url image to ImageView
        Glide.with(context)
            .load(currentItem.image)
            .apply(RequestOptions()
                .placeholder(R.drawable.outline_broken_image)
                .error(R.drawable.outline_broken_image)
            )
            .into(holder.imageViewRecipeItem)

        //Set the title
        holder.textViewRecipeNameItem.text = currentItem.title
    }

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewRecipeItem: ImageView = itemView.findViewById(R.id.imageViewRecipeItem)
        val textViewRecipeNameItem: TextView = itemView.findViewById(R.id.textViewRecipeNameItem)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    val clickedItem = itemList[position]

                    val intent = Intent(context, RecipeDetailActivity::class.java)
                    intent.putExtra(Constants.PARAM_RECIPE_ID, clickedItem.id)

                    context.startActivity(intent)
                }
            }
        }
    }
}