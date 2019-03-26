package user_registration.domain;

public interface UserRepository {
    void save(User user);

    User findByEmail(String email);
}
