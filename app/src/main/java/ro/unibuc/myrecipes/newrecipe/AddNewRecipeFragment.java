package ro.unibuc.myrecipes.newrecipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ro.unibuc.myrecipes.R;

public class AddNewRecipeFragment extends Fragment {

    public AddNewRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_recipe, container, false);
    }
}