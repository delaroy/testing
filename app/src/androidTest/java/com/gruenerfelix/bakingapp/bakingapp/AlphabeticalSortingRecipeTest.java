package com.gruenerfelix.bakingapp.bakingapp;

import android.support.test.espresso.core.deps.guava.collect.Ordering;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gruenerfelix.bakingapp.bakingapp.adapter.TestAdapter;
import com.gruenerfelix.bakingapp.bakingapp.model.Recipe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by delaroy on 6/27/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AlphabeticalSortingRecipeTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);



    @Test
    public void Sorted() {

        onView(withId(R.id.recycler_view)).check(matches(isSortedAlphabetically()));
    }

    private static Matcher<View> isSortedAlphabetically() {
        return new TypeSafeMatcher<View>() {

            private final List<String> recipeName = new ArrayList<>();
            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                TestAdapter recipeAdapter = (TestAdapter) recyclerView.getAdapter();
                recipeName.clear();
                recipeName.addAll(extractRecipeNames(recipeAdapter.getRecipe()));
                return Ordering.natural().isOrdered(recipeName);
            }

            private List<String> extractRecipeNames(List<Recipe> recipes) {
                List<String> recipeNames = new ArrayList<>();
                for (Recipe recipe : recipes) {
                    recipeNames.add(recipe.getName());
                }
                return recipeName;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has items sorted alphabetically: " + recipeName);
            }
        };
    }




}