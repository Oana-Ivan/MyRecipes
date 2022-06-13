package ro.unibuc.myrecipes.newrecipe;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ro.unibuc.myrecipes.R;
import ro.unibuc.myrecipes.models.Recipe;
import ro.unibuc.myrecipes.recipes.AllRecipesFragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AddNewRecipeFragment extends Fragment {
    private EditText titleET, ingredientsET, stepsET;
    private ImageView recipeImage;
    private AppCompatButton addRecipeBtn;

    private String recipeID, title, ingredients, steps, image;

    // cloud database
    public FirebaseFirestore firestoreDB;
    public CollectionReference recipesCollection;
    private int collectionSize = 0;

    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private String imageURL = null;
    private byte[] byteArrayRecipeImage;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public AddNewRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_recipe, container, false);
        initViews(view);
        firestoreInit();

        // get the number of recipes in collection
        recipesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    collectionSize++;
                }
                collectionSize++;
                recipeID = "recipe_" + collectionSize;

                progressDialog = new ProgressDialog(getContext());
                recipeImage.setOnClickListener(v -> {
                    dispatchTakePictureIntent();
                });

                addRecipeBtn.setOnClickListener(btn -> {
                    assignData();
                    if (!emptyFields()) {
                        progressDialog.setMessage("Storing Data...");
                        progressDialog.show();

                        // TODO Check if image is null
                        UploadTask image_path = storageReference.child("recipe_image").child(recipeID + ".jpg").putBytes(byteArrayRecipeImage);
//                        imageURL = storageReference.child("recipe_image").child(recipeID + ".jpg").toString();

                        Task<Uri> urlTask = image_path.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task1) throws Exception {
                                if (!task1.isSuccessful()) {
                                    throw task1.getException();
                                }

                                // Continue with the task to get the download URL
                                return storageReference.child("recipe_image").child(recipeID + ".jpg").getDownloadUrl();
                            }
                        }).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Uri downloadUri = task1.getResult();
                                image = downloadUri.toString();

                                Recipe newRecipe = new Recipe(recipeID, image, title, ingredients, steps);
                                recipesCollection.document(recipeID).set(newRecipe).addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Recipe Data Stored Successfully", Toast.LENGTH_LONG).show();

                                        // Redirect to All recipes
                                        AllRecipesFragment allRecipesFragment = new AllRecipesFragment();
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.container, allRecipesFragment);
                                        ft.commit();
                                    } else {
                                        String error = task1.getException().getMessage();
                                        Toast.makeText(getContext(), "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                                    }
                                    progressDialog.dismiss();
                                });
                            }
                        });
                    }
                });
            }
            else {
                Log.d(TAG, "Error getting documents: ", task.getException());
                collectionSize = 0;
            }
        });

        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            recipeImage.setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArrayRecipeImage = stream.toByteArray();
        }
    }

    private void firestoreInit() {
        firestoreDB = FirebaseFirestore.getInstance();
        recipesCollection = firestoreDB.collection("Recipes");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private boolean emptyFields() {
        if (!title.isEmpty() && !ingredients.isEmpty() && !steps.isEmpty()) {
            return false;
        }

        Toast.makeText(getContext(), "All fields required!", Toast.LENGTH_LONG).show();
        return true;
    }

    private void assignData() {
        title = titleET.getText().toString();
        ingredients = ingredientsET.getText().toString();
        steps = stepsET.getText().toString();
    }

    private void initViews(View view) {
        titleET = view.findViewById(R.id.fragment_add_new_recipe_et_title);
        ingredientsET = view.findViewById(R.id.fragment_add_new_recipe_et_ingredients);
        stepsET = view.findViewById(R.id.fragment_add_new_recipe_et_steps);
        recipeImage = view.findViewById(R.id.fragment_add_new_recipe_img);
        addRecipeBtn = view.findViewById(R.id.fragment_add_new_recipe_btn);
    }
}