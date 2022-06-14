package ro.unibuc.myrecipes.user.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecentViewedRecipesDao {
    @Query("SELECT * FROM recent_viewed_recipes")
    List<RecentViewedRecipes> getAll();

    @Query("SELECT count(*) FROM recent_viewed_recipes")
    int getSize();

    @Query("SELECT * FROM recent_viewed_recipes WHERE user_id IN (:userIds)")
    List<RecentViewedRecipes> loadAllByUserIds(int[] userIds);

    @Query("SELECT * FROM recent_viewed_recipes WHERE username LIKE :username LIMIT 1")
    RecentViewedRecipes findByUsername(String username);

    @Query("SELECT * FROM recent_viewed_recipes WHERE username LIKE :username LIMIT 5")
    List<RecentViewedRecipes> loadFirst5ByUsername(String username);

    @Insert
    void insertAll(RecentViewedRecipes... recipes);

    @Delete
    void delete(RecentViewedRecipes recipe);
}
