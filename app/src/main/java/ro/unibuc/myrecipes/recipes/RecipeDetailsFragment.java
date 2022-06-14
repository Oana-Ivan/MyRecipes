package ro.unibuc.myrecipes.recipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ro.unibuc.myrecipes.R;
import ro.unibuc.myrecipes.models.Recipe;
import ro.unibuc.myrecipes.models.User;
import ro.unibuc.myrecipes.user.room.AppDatabase;
import ro.unibuc.myrecipes.user.room.RecentViewedRecipes;
import ro.unibuc.myrecipes.user.room.RecentViewedRecipesDao;
import ro.unibuc.myrecipes.user.room.UserDao;

import static ro.unibuc.myrecipes.user.UserLoginFragment.USERNAME;
import static ro.unibuc.myrecipes.user.UserLoginFragment.USER_PREFERENCES;

public class RecipeDetailsFragment extends Fragment {
    private Recipe currentRecipe;
    private ImageView recipeImage;
    private TextView titleTV, ingredientsTV, stepsTV;
    private AppCompatButton sendRecipeBtn;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        initViews(view);

        Bundle bundle = getArguments();
        currentRecipe = (Recipe) bundle.getSerializable("Recipe");

        assignData(currentRecipe);

        incrementUserCount();

        sendRecipeBtn.setOnClickListener(btn -> {
            String recipe = recipeToString(currentRecipe);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, recipe);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        return view;
    }

    private void incrementUserCount() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(USERNAME, "");

        AppDatabase roomDB = Room.databaseBuilder(getContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        UserDao userDao = roomDB.userDao();
        RecentViewedRecipesDao recentViewedRecipesDao = roomDB.recentViewedRecipesDao();

        List<User> users = userDao.getAll();
        List<RecentViewedRecipes> recentViewedRecipes = recentViewedRecipesDao.getAll();

        RecentViewedRecipes newRecentViewedRecipes = new RecentViewedRecipes(recentViewedRecipesDao.getSize(), userDao.findByUserIdByUsername(username), username, currentRecipe.getId(), currentRecipe.getTitle());

        recentViewedRecipesDao.insertAll(newRecentViewedRecipes);
    }

    private String recipeToString(Recipe currentRecipe) {
        return currentRecipe.getTitle() + "\nIngredients: \n" + currentRecipe.getIngredients() + "\nSteps: \n" + currentRecipe.getSteps();
    }

    private void assignData(Recipe currentRecipe) {
        // Set animal image
        if (currentRecipe.getImage() != null) {
            Glide.with(this).load(currentRecipe.getImage()).into(recipeImage);
        }
        else {
//            recipeImage.setBackgroundResource(R.drawable.recipe);
        }

        titleTV.setText(currentRecipe.getTitle());
        ingredientsTV.setText(currentRecipe.getIngredients());
        stepsTV.setText(currentRecipe.getSteps());
    }

    private void initViews(View view) {
        recipeImage = view.findViewById(R.id.fragment_recipe_details_img);
        titleTV = view.findViewById(R.id.fragment_recipe_details_tv_title);
        ingredientsTV = view.findViewById(R.id.fragment_recipe_details_tv_ingredients_2);
        stepsTV = view.findViewById(R.id.fragment_recipe_details_tv_steps);
        sendRecipeBtn = view.findViewById(R.id.fragment_recipe_details_btn_send_recipe);
    }
}