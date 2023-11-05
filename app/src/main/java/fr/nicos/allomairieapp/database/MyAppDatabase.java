package fr.nicos.allomairieapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fr.nicos.allomairieapp.database.dao.UserDao;
import fr.nicos.allomairieapp.database.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}

