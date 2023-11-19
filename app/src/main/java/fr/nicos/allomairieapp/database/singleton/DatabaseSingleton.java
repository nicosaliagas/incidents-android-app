package fr.nicos.allomairieapp.database.singleton;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import fr.nicos.allomairieapp.database.MyAppDatabase;

public class DatabaseSingleton {
    private static MyAppDatabase instance;

    private DatabaseSingleton() { }

    static RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    public static synchronized MyAppDatabase getInstance(Context context) {
        if (instance == null) {
            // Create a new instance of your Room database
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MyAppDatabase.class, "AllomairieDB")
                    .addCallback(DatabaseSingleton.myCallBack)
                    .build();
        }
        return instance;
    }
}

