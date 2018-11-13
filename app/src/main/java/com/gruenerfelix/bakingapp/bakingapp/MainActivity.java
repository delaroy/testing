package com.gruenerfelix.bakingapp.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.gruenerfelix.bakingapp.bakingapp.adapter.RecipeAdapter;
import com.gruenerfelix.bakingapp.bakingapp.adapter.TestAdapter;
import com.gruenerfelix.bakingapp.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.gruenerfelix.bakingapp.bakingapp.networking.api.Service;
import com.gruenerfelix.bakingapp.bakingapp.networking.generators.DataServiceGenerator;
import com.gruenerfelix.bakingapp.bakingapp.sync.RecipeSyncUtils;
import com.gruenerfelix.bakingapp.bakingapp.utils.SimpleIdlingResource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "extra_recipe";
    private static final String EXTRA_RECIPE_LIST = "extra_recipes_list";
    private List<Recipe> recipes = new ArrayList<>();
    private ArrayList<Recipe> recipesInstance = new ArrayList<>();

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private RecipeAdapter recipeAdapter;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recycler_view.setHasFixedSize(true);
        recipeAdapter = new RecipeAdapter(this, null);

        RecipeSyncUtils.initialize(this);

        if (savedInstanceState != null) {
            recipesInstance = savedInstanceState.getParcelableArrayList(EXTRA_RECIPE_LIST);
            displayData();
        } else {
            initViews();

            //For testing the recipe collection sorting alphabetically
            TestAdapter testAdapter = new TestAdapter(LayoutInflater.from(this));
            recycler_view.setAdapter(testAdapter);
            testAdapter.setRecipe(recipes);
        }

    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private void displayData(){
        recipeAdapter = new RecipeAdapter(this, recipesInstance);
        if (isTablet(this)) {

            recycler_view.setLayoutManager(new GridLayoutManager(this, 3));

        } else {

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recycler_view.setLayoutManager(layoutManager);
        }
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(recipeAdapter);
        recipeAdapter.notifyDataSetChanged();
    }


    private void initViews(){
        if (isTablet(this)) {

            recycler_view.setLayoutManager(new GridLayoutManager(this, 3));

        } else {

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recycler_view.setLayoutManager(layoutManager);
        }

        recycler_view.setItemAnimator(new DefaultItemAnimator());
        loadData();
    }

    private void loadData() {
        Service service = DataServiceGenerator.createService(Service.class);
        Call<List<Recipe>> call = service.fetchData();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        recipes = response.body();
                        recipesInstance.clear();
                        recipesInstance.addAll(recipes);
                        recycler_view.setAdapter(new RecipeAdapter(getApplicationContext(), recipes));
                        recycler_view.smoothScrollToPosition(0);

                        //sorting recipe in alphabetical order which UI test was done upon
                        Collections.sort(recipes, Recipe.BY_NAME_ALPHABETICAL);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_RECIPE_LIST, recipesInstance);

    }
}
