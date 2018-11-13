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
import com.gruenerfelix.bakingapp.bakingapp.model.Recipe;
import com.gruenerfelix.bakingapp.bakingapp.utils.JsonUtils;
import com.gruenerfelix.bakingapp.bakingapp.utils.PreferenceUtil;

import java.util.List;

public class RecipeListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeListRemoteViewsFactory(getApplicationContext());
    }
}

class RecipeListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Recipe> recipes;

    public RecipeListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        int selectedRecipeId = PreferenceUtil.getSelectedRecipeId(mContext);
        Cursor recipeCursor = mContext.getContentResolver().query(
                RecipeProvider.Recipes.CONTENT_URI,
                null,
                null,
                null,
                RecipeContract.RecipeEntry.COLUMN_ID
        );

        recipes = JsonUtils.getRecipeListFromCursor(recipeCursor, mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipes != null ? recipes.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Recipe recipe = recipes.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
        views.setTextViewText(R.id.textview_ingredient_summary, recipe.getName());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(MainActivity.EXTRA_RECIPE, recipe);
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
