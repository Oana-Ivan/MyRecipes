package ro.unibuc.myrecipes.user.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.unibuc.myrecipes.models.User;

@Database(entities = {User.class, RecentViewedRecipes.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract RecentViewedRecipesDao recentViewedRecipesDao();
}