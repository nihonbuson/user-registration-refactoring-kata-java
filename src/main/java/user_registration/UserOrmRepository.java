package user_registration;

import java.util.HashMap;

public class UserOrmRepository {
    public HashMap<String, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getEmail(), user);
    }

    public User findByEmail(String email) {
        return users.getOrDefault(email, null);
    }
}
