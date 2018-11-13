package com.gruenerfelix.bakingapp.bakingapp.utils;


import java.io.IOException;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class NetworkUtils {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    public static String getResponseFromHttpUrl() throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RecipeApi api = retrofit.create(RecipeApi.class);

        Call<String> call = api.getAllRecipes();
        return  call.execute().body();
    }
}

