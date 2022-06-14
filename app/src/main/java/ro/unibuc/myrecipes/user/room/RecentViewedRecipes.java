package ro.unibuc.myrecipes.user.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_viewed_recipes")
public class RecentViewedRecipes {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "user_id")
    public String userID;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "recipe_id")
    public String recipeID;

    @ColumnInfo(name = "recipe_title")
    public String recipeTitle;

    public RecentViewedRecipes(int id, String userID, String username, String recipeID, String recipeTitle) {
        this.id = id;
        this.userID = userID;
        this.username = username;
        this.recipeID = recipeID;
        this.recipeTitle = recipeTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }
}
