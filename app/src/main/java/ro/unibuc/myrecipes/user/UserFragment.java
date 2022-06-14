package ro.unibuc.myrecipes.user;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import java.util.List;

import ro.unibuc.myrecipes.R;
import ro.unibuc.myrecipes.models.User;
import ro.unibuc.myrecipes.user.room.AppDatabase;
import ro.unibuc.myrecipes.user.room.RecentViewedRecipes;
import ro.unibuc.myrecipes.user.room.RecentViewedRecipesDao;
import ro.unibuc.myrecipes.user.room.UserDao;

import static ro.unibuc.myrecipes.user.UserLoginFragment.PASSWORD;
import static ro.unibuc.myrecipes.user.UserLoginFragment.USERNAME;
import static ro.unibuc.myrecipes.user.UserLoginFragment.USER_PREFERENCES;

public class UserFragment extends Fragment {
    private static final String LOGIN_REGISTER = "Login or register";
    private TextView usernameTV, recipesTV, logoutTV;
    private ImageView welcomeImage;

    String username, recipes;

    AppDatabase roomDB;
    UserDao userDao;
    RecentViewedRecipesDao recentViewedRecipesDao;
    List<User> users;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initViews(view);

        // Creating Animations using ObjectAnimators
        ObjectAnimator AnimateGlass1 = ObjectAnimator.ofFloat(welcomeImage, "translationY", 1000f);
        AnimateGlass1.setDuration(6000);
        AnimateGlass1.start();

        initDB();
        assignData();

        logoutTV.setOnClickListener(l -> {
            logout();
        });

        return view;
    }

    private void initViews(View view) {
        welcomeImage = view.findViewById(R.id.fragment_user_welcome);
        usernameTV = view.findViewById(R.id.fragment_user_username_tv);
        recipesTV = view.findViewById(R.id.fragment_user_recipes_tv);
        logoutTV = view.findViewById(R.id.fragment_user_logout);
    }

    private void assignData() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        username = sharedpreferences.getString(USERNAME, "");

        if (!username.isEmpty()) {
            usernameTV.setText(username.toUpperCase());
            logoutTV.setVisibility(View.VISIBLE);

            // get recipes from database
            List<RecentViewedRecipes> recipesList = recentViewedRecipesDao.loadFirst5ByUsername(username);
            recipes = "";
            for (RecentViewedRecipes r : recipesList) {
                recipes += "\n- " + r.recipeTitle;
            }
            recipesTV.setText(recipes);
        }
        else {
            usernameTV.setText(LOGIN_REGISTER);
            logoutTV.setVisibility(View.INVISIBLE);

            usernameTV.setOnClickListener(u -> {
                // Redirect to login/register page
                UserLoginFragment userLoginFragment = new UserLoginFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, userLoginFragment);
                ft.commit();
            });
        }
    }

    private void initDB() {
        roomDB = Room.databaseBuilder(getContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        userDao = roomDB.userDao();
        recentViewedRecipesDao = roomDB.recentViewedRecipesDao();
        users = userDao.getAll();
    }

    private void logout() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, "");
        editor.putString(PASSWORD, "");
        editor.apply();

        // Redirect to login/register page
        UserFragment userLoginFragment = new UserFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, userLoginFragment);
        ft.commit();
    }
}