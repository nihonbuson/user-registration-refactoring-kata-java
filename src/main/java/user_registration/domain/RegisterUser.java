package user_registration.domain;

import user_registration.infrastructure.EmailSender;

import java.util.Random;

public class RegisterUser {

    private final UserRepository userRepository;
    private final EmailSender emailSender;

    public RegisterUser(UserRepository userOrmRepository) {
        userRepository = userOrmRepository;
        emailSender = new EmailSender();
    }

    public User execute(String password, String email, String name) throws PasswordIsNotValidException, EmailIsAlreadyInUseException, InvalidEmailException {
        if (password.length() <= 8 || !password.contains("_")) {
            throw new PasswordIsNotValidException();
        }

        if (userRepository.findByEmail(email) != null) {
            throw new EmailIsAlreadyInUseException();
        }

        User user = new User(
                new Random().nextInt(),
                name,
                email,
                password
        );
        userRepository.save(user);

        emailSender.send(email, "Welcome to Codium", "This is the confirmation email");

        return user;
    }

}
