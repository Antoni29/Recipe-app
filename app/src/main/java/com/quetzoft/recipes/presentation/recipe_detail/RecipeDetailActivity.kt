package com.quetzoft.recipes.presentation.recipe_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.quetzoft.recipes.R
import com.quetzoft.recipes.common.Constants
import com.quetzoft.recipes.domain.model.Ingredient
import com.quetzoft.recipes.presentation.recipe_detail.adapter.IngredientListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var recipeImageView: ImageView
    private lateinit var recipeTitle: TextView
    private lateinit var readyInMinutesTextView: TextView
    private lateinit var healthScoreTextView: TextView
    private lateinit var recipeDescriptionTextView: TextView
    private lateinit var recipeIngredientsRecyclerView: RecyclerView
    private lateinit var ingredientList: ArrayList<Ingredient>
    private lateinit var adapter: IngredientListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)
        val toolbar = findViewById<Toolbar>(R.id.recipeDetailToolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)

        }

        viewModel = ViewModelProvider(this)[RecipeDetailViewModel::class.java]
        recipeImageView = findViewById(R.id.collapsingToolbarImage)
        recipeTitle = findViewById(R.id.recipeTitle)
        readyInMinutesTextView = findViewById(R.id.readyInMinutesTextView)
        healthScoreTextView = findViewById(R.id.healthScoreTextView)
        recipeDescriptionTextView = findViewById(R.id.recipeDescriptionTextView)
        //Ingredients RecyclerView
        recipeIngredientsRecyclerView = findViewById(R.id.recipeIngredientsRecyclerView)
        ingredientList = arrayListOf()
        adapter = IngredientListAdapter(ingredientList)
        recipeIngredientsRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeIngredientsRecyclerView.adapter = adapter

        val recipeId = intent.extras?.getInt(Constants.PARAM_RECIPE_ID)
        viewModel.getRecipeDetail(recipeId ?: 0)

        observers()
    }

    private fun observers() {
        viewModel.recipe.observe(this) { recipe ->
            recipe?.let {  recipeDetail ->

                Glide.with(this)
                    .load(recipeDetail.image)
                    .apply(
                        RequestOptions()
                        .placeholder(R.drawable.outline_broken_image)
                        .error(R.drawable.outline_broken_image)
                    )
                    .into(recipeImageView)

                recipeTitle.text = recipeDetail.title
                readyInMinutesTextView.text = "${recipeDetail.readyInMinutes} min."
                healthScoreTextView.text = "Health score: ${recipeDetail.healthScore}"
                recipeDescriptionTextView.text = Html.fromHtml(recipeDetail.summary)

                ingredientList.addAll(recipeDetail.extendedIngredients)
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.isLoading.observe(this) {

        }

        viewModel.error.observe(this) { error ->
            if(error.isNotEmpty())
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}