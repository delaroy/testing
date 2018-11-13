package com.gruenerfelix.bakingapp.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.gruenerfelix.bakingapp.bakingapp.MainActivity;
import com.gruenerfelix.bakingapp.bakingapp.R;
import com.gruenerfelix.bakingapp.bakingapp.data.RecipeContract;
import com.gruenerfelix.bakingapp.bakingapp.data.RecipeProvider;
import com.gruenerfelix.bakingapp.bakingapp.model.Ingredient;
import com.gruenerfelix.bakingapp.bakingapp.model.Recipe;
import com.gruenerfelix.bakingapp.bakingapp.utils.JsonUtils;
import com.gruenerfelix.bakingapp.bakingapp.utils.PreferenceUtil;

import java.util.ArrayList;

public class IngredientListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientListRemoteViewsFactory(getApplicationContext());
    }
}

class IngredientListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Recipe recipe;

    public IngredientListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        int selectedRecipeId = PreferenceUtil.getSelectedRecipeId(mContext);
        Cursor recipeCursor = mContext.getContentResolver().query(
                RecipeProvider.Recipes.withId(selectedRecipeId),
                null,
                null,
                null,
                RecipeContract.RecipeEntry.COLUMN_ID
        );

        ArrayList<Recipe> recipeList = JsonUtils.getRecipeListFromCursor(recipeCursor, mContext);

        recipe = recipeList.get(0);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe != null && recipe.getIngredients() != null ?
                recipe.getIngredients().size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = recipe.getIngredients().get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
        String quantity = Double.toString(ingredient.getQuantity());
        String recipeIngredient = quantity + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();

        views.setTextViewText(R.id.textview_ingredient_summary, recipeIngredient);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(MainActivity.EXTRA_RECIPE, recipe);
        //fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.textview_ingredient_summary, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
