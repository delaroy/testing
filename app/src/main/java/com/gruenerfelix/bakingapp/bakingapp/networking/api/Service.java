package com.gruenerfelix.bakingapp.bakingapp.networking.api;

import com.gruenerfelix.bakingapp.bakingapp.model.Recipe;
import com.gruenerfelix.bakingapp.bakingapp.networking.Routes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    @GET(Routes.END_POINT)
    Call<List<Recipe>> fetchData();

}
