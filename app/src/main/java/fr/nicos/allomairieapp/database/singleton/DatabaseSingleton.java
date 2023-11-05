package fr.nicos.allomairieapp.database.singleton;

import android.content.Context;

import androidx.room.Room;

import fr.nicos.allomairieapp.database.MyAppDatabase;

public class DatabaseSingleton {
    private static MyAppDatabase instance;

    private DatabaseSingleton() { }

    public static synchronized MyAppDatabase getInstance(Context context) {
        if (instance == null) {
            // Create a new instance of your Room database
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MyAppDatabase.class, "allo-database")
                    .build();
        }
        return instance;
    }
}

