package ro.unibuc.myrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ro.unibuc.myrecipes.newrecipe.AddNewRecipeFragment;
import ro.unibuc.myrecipes.recipes.AllRecipesFragment;
import ro.unibuc.myrecipes.user.UserFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    AllRecipesFragment allRecipesFragment = new AllRecipesFragment();
    AddNewRecipeFragment addNewRecipeFragment = new AddNewRecipeFragment();
    UserFragment userFragment = new UserFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.user_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_recipes:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, allRecipesFragment).commit();
                return true;

            case R.id.add_recipe:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, addNewRecipeFragment).commit();
                return true;

            case R.id.user_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, userFragment).commit();
                return true;
        }
        return false;
    }
}