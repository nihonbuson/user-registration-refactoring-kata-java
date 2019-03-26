package user_registration.application;

import user_registration.domain.*;

public class RegisterUser {

    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final RandomIdGenerator randomIdGenerator;

    public RegisterUser(UserRepository userOrmRepository, EmailSender emailSender, RandomIdGenerator randomIdGenerator) {
        userRepository = userOrmRepository;
        this.emailSender = emailSender;
        this.randomIdGenerator = randomIdGenerator;
    }

    public User execute(String password, String emailAddress, String name) throws PasswordIsNotValidException, EmailAddressIsAlreadyInUseException, EmailException {
        if (password.length() <= 8 || !password.contains("_")) {
            throw new PasswordIsNotValidException();
        }

        if (userRepository.findByEmail(emailAddress) != null) {
            throw new EmailAddressIsAlreadyInUseException();
        }

        User user = new User(
                randomIdGenerator.generateId(),
                name,
                emailAddress,
                password
        );
        userRepository.save(user);

        Email confirmationEmail = new Email(emailAddress, "Welcome to Codium", "This is the confirmation email");
        emailSender.send(confirmationEmail);

        return user;
    }

}
