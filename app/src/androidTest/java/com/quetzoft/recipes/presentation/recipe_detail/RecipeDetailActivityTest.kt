package com.quetzoft.recipes.presentation.recipe_detail

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.quetzoft.recipes.R
import com.quetzoft.recipes.presentation.home.HomeActivity
import com.quetzoft.recipes.presentation.recipe_map.RecipeMapActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RecipeDetailActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(HomeActivity::class.java)

    @Test
    fun whenClicksOnGoToLocationButton() {
        //Wait for list response
        Thread.sleep(1500)
        //Click item at position 1
        onView(withId(R.id.recipesRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click())
        )
        //Check if RecipeDetailActivity is displaying
        intended(IntentMatchers.hasComponent(RecipeDetailActivity::class.java.name))

        //Wait for detail response
        Thread.sleep(1000)

        //Click button
        onView(withId(R.id.goToLocationButton)).perform(scrollTo(), click())
        //Check if RecipeMapActivity is displaying
        intended(IntentMatchers.hasComponent(RecipeMapActivity::class.java.name))

        //Wait for a second con map view
        Thread.sleep(1000)

        //Go back and check id location button is showing
        pressBack()
        onView(withId(R.id.goToLocationButton)).check(matches(isDisplayed()))
    }

}