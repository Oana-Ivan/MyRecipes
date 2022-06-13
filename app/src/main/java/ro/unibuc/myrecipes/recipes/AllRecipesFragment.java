package ro.unibuc.myrecipes.recipes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import ro.unibuc.myrecipes.R;
import ro.unibuc.myrecipes.models.Recipe;

import static android.content.ContentValues.TAG;

public class AllRecipesFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recipesRV;
    private AllRecipesAdapter recipesAdapter;
    private RecyclerView.LayoutManager recipesLayoutManager;

    private ArrayList<Recipe> recipes, recipesAll;

    // cloud database
    public FirebaseFirestore db;
    public CollectionReference recipesCollection;

    public AllRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_recipes, container, false);

        searchView = view.findViewById(R.id.fragment_all_recipes_search_view);
        recipesRV = view.findViewById(R.id.fragment_all_recipes_rv);
        recipesRV.setHasFixedSize(true);
        recipesLayoutManager = new LinearLayoutManager(getContext());

        // Retrieve recipes data from Firestore
        db = FirebaseFirestore.getInstance();
        recipesCollection = db.collection("Recipes");
        recipesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recipes = new ArrayList<>();
                for (QueryDocumentSnapshot currentRecipeDocument : task.getResult()) {
                    recipes.add(currentRecipeDocument.toObject(Recipe.class));
                }
                recipesAll = recipes;
                recipesAdapter = new AllRecipesAdapter(recipes);
                recipesRV.setLayoutManager(recipesLayoutManager);
                recipesRV.setAdapter(recipesAdapter);

                // search
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filter(newText);
                        return false;
                    }
                });

                // click on recipe
                recipesAdapter.setOnItemClickListener(position -> {
                    // Redirect to recipe page
                    FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();

                    Bundle bundle = new Bundle();
                    Recipe recipe = recipes.get(position);
                    bundle.putSerializable("Recipe", recipe);
                    recipeDetailsFragment.setArguments(bundle);
                    ft.replace(R.id.container, recipeDetailsFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                });

                Log.d(TAG, recipes.toString());
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<Recipe> filteredList = new ArrayList<>();

        for (Recipe item : recipesAll) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())
                    || item.getIngredients().toLowerCase().contains(text.toLowerCase())
                    || item.getSteps().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }

        recipes = filteredList;
        recipesAdapter.filterList(recipes);

        if (text.equals("")) {
            recipesAdapter.filterList(recipesAll);
        }
        recipesAdapter.notifyDataSetChanged();
    }

}