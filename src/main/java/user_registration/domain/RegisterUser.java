package user_registration.domain;

import java.util.Random;

public class RegisterUser {

    private final UserRepository userRepository;
    private final EmailSender emailSender;

    public RegisterUser(UserRepository userOrmRepository, EmailSender emailSender) {
        userRepository = userOrmRepository;
        this.emailSender = emailSender;
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

        Email confirmationEmail = new Email(email, "Welcome to Codium", "This is the confirmation email");
        emailSender.send(confirmationEmail);

        return user;
    }

}
