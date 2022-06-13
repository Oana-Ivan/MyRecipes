package ro.unibuc.myrecipes.recipes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ro.unibuc.myrecipes.R;
import ro.unibuc.myrecipes.models.Recipe;

import static android.content.ContentValues.TAG;

public class AllRecipesAdapter extends RecyclerView.Adapter<AllRecipesAdapter.RecipesViewHolder> {
    private ArrayList<Recipe> recipesList;
    private AllRecipesAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AllRecipesAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public AllRecipesAdapter(ArrayList<Recipe> recipesList) {
        this.recipesList = recipesList;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Recipe> filterList) {
        recipesList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        RecipesViewHolder  recipesViewHolder = new RecipesViewHolder(view, onItemClickListener);
        return recipesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipe recipe = recipesList.get(position);

        String title = recipe.getTitle();
        holder.recipeTitle.setText(title);
        if (recipe.getImage() != null) {
            RequestOptions myOptions = new RequestOptions()
                    .override(100, 100);
            Glide.with(holder.recipeImg.getContext())
                    .asBitmap()
                    .apply(myOptions)
                    .load(recipe.getImage())
                    .into(holder.recipeImg);
        } else {
            holder.recipeImg.setBackgroundResource(R.drawable.recipe);
        }

    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTitle;
        public ImageView recipeImg;
        public LinearLayout item;

        public RecipesViewHolder(@NonNull View itemView, AllRecipesAdapter.OnItemClickListener listener) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.recipe_item_title);
            recipeImg = itemView.findViewById(R.id.recipe_item_img);
            item = itemView.findViewById(R.id.recipe_item_ll);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
