package fr.nicos.allomairieapp.database.singleton;

import java.util.ArrayList;
import java.util.List;

import fr.nicos.allomairieapp.database.entity.User;

public class DataGenerator {
    public static List<User> generateUsers() {
        List<User> users = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setUid(i);
            user.setFirstName("Nico " + i);
            user.setLastName("Aliagas "+ i);
        }
        return users;
    }
}
