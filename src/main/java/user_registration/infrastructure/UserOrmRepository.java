package user_registration.infrastructure;

import user_registration.domain.User;
import user_registration.domain.UserRepository;

import java.util.HashMap;

public class UserOrmRepository implements UserRepository {
    public HashMap<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public User findByEmail(String email) {
        return users.getOrDefault(email, null);
    }
}
