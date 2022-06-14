package ro.unibuc.myrecipes.user.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ro.unibuc.myrecipes.models.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT count(*) FROM user")
    int getSize();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE username LIKE :username AND " +
            "password LIKE :password LIMIT 1")
    User findByUsernameAndPassword(String username, String password);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}