package com.gruenerfelix.bakingapp.bakingapp.utils;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApi {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<String> getAllRecipes();
}
