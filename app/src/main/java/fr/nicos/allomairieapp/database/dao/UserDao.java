package fr.nicos.allomairieapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.nicos.allomairieapp.database.entity.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("DELETE FROM user")
    void nukeUserTable();

    @Insert
    public void addUser(User user);

    @Delete
    void delete(User user);
}
