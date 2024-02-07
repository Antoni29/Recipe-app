package com.quetzoft.recipes.presentation.home

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.quetzoft.recipes.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.quetzoft.recipes.presentation.recipe_detail.RecipeDetailActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(HomeActivity::class.java)


    @Test
    fun whenClicksCuisinesRecyclerViewItem() {
        val itemPosition = 1
        //Click item at position 1
        onView(withId(R.id.cuisinesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemPosition, click())
        )
        //Check if Cuisines at position 1 was selected
        val recyclerView = intentsTestRule.activity.findViewById<View>(R.id.cuisinesRecyclerView) as RecyclerView
        val itemContainer = recyclerView.findViewHolderForAdapterPosition(itemPosition)!!.itemView as CardView
        val colorCode = (itemContainer.background.current as ColorDrawable).color
        assert(colorCode == ContextCompat.getColor(itemContainer.context, R.color.gray))
    }

    @Test
    fun whenClicksRecipesRecyclerViewItem() {
        //Wait for response
        Thread.sleep(1500)
        //Click item at position 1
        onView(withId(R.id.recipesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click())
        )
        //Check if RecipeDetailActivity is displaying
        intended(hasComponent(RecipeDetailActivity::class.java.name))
    }
}