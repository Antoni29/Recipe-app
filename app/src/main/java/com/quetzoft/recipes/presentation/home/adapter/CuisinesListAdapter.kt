package com.quetzoft.recipes.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.quetzoft.recipes.R

class CuisinesListAdapter(chosenItem: Int, private val itemList: List<String>) :
    RecyclerView.Adapter<CuisinesListAdapter.ItemViewHolder>() {

    private lateinit var context: Context
    var onItemClick: ((String, Int) -> Unit)? = null
    var selectedItem: Int = chosenItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cuisines_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.textViewCuisines.text = currentItem
        if(selectedItem == position)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    }

    override fun getItemCount(): Int = itemList.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCuisines: TextView = itemView.findViewById(R.id.textViewCuisines)
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition], adapterPosition)
                notifyItemChanged(selectedItem)
                selectedItem = adapterPosition
                notifyItemChanged(selectedItem)
            }
            itemView.isSelected
        }
    }
}