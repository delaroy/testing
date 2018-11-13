package com.gruenerfelix.bakingapp.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.gruenerfelix.bakingapp.bakingapp.R;
import com.gruenerfelix.bakingapp.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by delaroy on 6/16/17.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {

    private final LayoutInflater layoutInflater;

    private final List<Recipe> teams;



    public TestAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.teams = new ArrayList<>();
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(layoutInflater.inflate(R.layout.recipe_card, parent, false));
    }



    public List<Recipe> getRecipe() {
        return teams;
    }

    public void setRecipe(List<Recipe> recipeList) {
        teams.clear();
        teams.addAll(recipeList);
        notifyItemRangeInserted(0, recipeList.size());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }


    @Override
    public int getItemCount(){
        return teams.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        ImageView image;

        public MyViewHolder(View view){

            super(view);
            title = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.recipeImage);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                }
            });
        }
    }
}
