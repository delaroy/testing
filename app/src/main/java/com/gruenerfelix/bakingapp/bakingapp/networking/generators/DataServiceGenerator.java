package com.gruenerfelix.bakingapp.bakingapp.networking.generators;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gruenerfelix.bakingapp.bakingapp.BuildConfig;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import com.gruenerfelix.bakingapp.bakingapp.networking.Routes;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataServiceGenerator {

    public static <S> S createService(Class<S> serviceClass) {
        String token = "";
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Routes.BASE_URL);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .cache(null);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return  retrofit.create(serviceClass);
    }


}
