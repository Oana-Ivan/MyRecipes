package ro.unibuc.myrecipes.models;

import java.io.Serializable;

public class Recipe implements Serializable {
    private String id;
    private String imageURL;
    private String title;
    private String ingredients;
    private String steps;

    public Recipe() {
    }

    public Recipe(String id, String image, String title, String ingredients, String steps) {
        this.id = id;
        this.imageURL = image;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return imageURL;
    }

    public void setImage(String image) {
        this.imageURL = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}
