package ro.unibuc.myrecipes.user.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ro.unibuc.myrecipes.models.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}