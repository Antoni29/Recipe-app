package com.quetzoft.recipes.presentation.recipe_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quetzoft.recipes.R
import com.quetzoft.recipes.domain.model.Ingredient

class IngredientListAdapter(private val itemList: List<Ingredient>) :
    RecyclerView.Adapter<IngredientListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientListAdapter.ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientListAdapter.ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.ingredientTextView.text = currentItem.original
    }

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientTextView: TextView = itemView.findViewById(R.id.ingredientTextView)
    }
}