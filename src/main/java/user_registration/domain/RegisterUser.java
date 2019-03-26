package user_registration.domain;

import user_registration.infrastructure.JavaUtilRandomIdGenerator;

public class RegisterUser {

    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final JavaUtilRandomIdGenerator randomIdGenerator;

    public RegisterUser(UserRepository userOrmRepository, EmailSender emailSender, JavaUtilRandomIdGenerator randomIdGenerator) {
        userRepository = userOrmRepository;
        this.emailSender = emailSender;
        this.randomIdGenerator = randomIdGenerator;
    }

    public User execute(String password, String email, String name) throws PasswordIsNotValidException, EmailIsAlreadyInUseException, InvalidEmailException {
        if (password.length() <= 8 || !password.contains("_")) {
            throw new PasswordIsNotValidException();
        }

        if (userRepository.findByEmail(email) != null) {
            throw new EmailIsAlreadyInUseException();
        }

        User user = new User(
                randomIdGenerator.generateId(),
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
