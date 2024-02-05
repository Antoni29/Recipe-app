package com.quetzoft.recipes.presentation.home

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.quetzoft.recipes.R
import com.quetzoft.recipes.domain.model.Recipe
import com.quetzoft.recipes.presentation.home.adapter.CuisinesListAdapter
import com.quetzoft.recipes.presentation.home.adapter.RecipeListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var cuisinesRecyclerView: RecyclerView
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recipeListAdapter: RecipeListAdapter
    private lateinit var recipeListData: ArrayList<Recipe>
    private lateinit var cuisinesListAdapter: CuisinesListAdapter
    private val cuisinesListData = arrayListOf("All", "Asian", "Chinese", "French", "Indian",
        "Italian", "Japanese", "Mediterranean", "Mexican", "Thai")
    private var searchCuisines: String = ""
    private var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.mainToolbar);
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        //Cuisines recycler view
        cuisinesRecyclerView = findViewById(R.id.cuisinesRecyclerView)
        cuisinesRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        cuisinesListAdapter = CuisinesListAdapter(0, cuisinesListData)
        cuisinesListAdapter.onItemClick = { cuisinesString, _ ->
            recipeListData.clear()
            recipeListAdapter.notifyDataSetChanged()

            if(cuisinesString == "All") {
                searchQuery = ""
                searchCuisines = ""
                viewModel.getRecipes()
            } else {
                searchQuery = ""
                searchCuisines = cuisinesString
                viewModel.getRecipesByCuisine(cuisinesString.lowercase(), 0)
            }
        }
        cuisinesRecyclerView.adapter = cuisinesListAdapter

        //Recipe recycler view
        swipeRefresh = findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
        }
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        recipesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        recipeListData = arrayListOf()
        recipeListAdapter = RecipeListAdapter(this, recipeListData)
        recipesRecyclerView.adapter = recipeListAdapter
        recipesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // Check if the last visible item is close to the total item count
                if (lastVisibleItemPosition >= totalItemCount - 1) {
                    if(searchQuery.isNotEmpty())
                        viewModel.getRecipesByQuery(searchQuery, totalItemCount)
                    else if(searchCuisines.isNotEmpty())
                        viewModel.getRecipesByCuisine(searchCuisines, totalItemCount)
                    else
                        viewModel.getRecipesOffset(totalItemCount)
                }
            }
        })

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
            swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(this) { error ->
            if(error.isNotEmpty())
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        menu.findItem(R.id.search).icon?.setTint(resources.getColor(R.color.white))
        val component = ComponentName(this, MainActivity::class.java)
        val searchableInfo = searchManager.getSearchableInfo(component)
        searchView.setSearchableInfo(searchableInfo)

        return true
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            query?.let {  mQuery ->
                if(mQuery.isNotEmpty()) {
                    recipeListData.clear()
                    recipeListAdapter.notifyDataSetChanged()
                    searchQuery = mQuery
                    viewModel.getRecipesByQuery(mQuery, 0)
                }
            }
        }
    }
}