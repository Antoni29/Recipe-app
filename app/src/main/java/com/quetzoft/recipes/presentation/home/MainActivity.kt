package com.quetzoft.recipes.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quetzoft.recipes.R
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.presentation.home.adapter.RecipeListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var recipeListAdapter: RecipeListAdapter
    private lateinit var recipeListData: ArrayList<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        recipesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        recipeListData = arrayListOf()
        recipeListAdapter = RecipeListAdapter(recipeListData)
        recipesRecyclerView.adapter = recipeListAdapter

        observers()
    }

    private fun observers() {
        viewModel.recipes.observe(this) { recipes ->
            if(recipes.isNotEmpty()) {

                if(recipesRecyclerView.visibility == View.GONE)
                    recipesRecyclerView.visibility = View.VISIBLE

                recipeListData.addAll(recipes)
                recipeListAdapter.notifyDataSetChanged()
            }
            else {

            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if(isLoading) {

            }
        }

        viewModel.error.observe(this) { error ->
            if(error.isNotEmpty())
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}