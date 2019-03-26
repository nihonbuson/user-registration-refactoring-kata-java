package user_registration.domain;

import user_registration.infrastructure.UserOrmRepository;
import user_registration.infrastructure.UserRegistrationController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.Random;

public class RegisterUser {

    private final UserOrmRepository userRepository;

    public RegisterUser() {
        userRepository = UserRegistrationController.orm;
    }

    public User execute(String password, String email, String name) throws MessagingException, PasswordIsNotValidException, EmailIsAlreadyInUseException {
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

        Properties prop = new Properties();
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("smtpUsername", "smtpPassword");
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("noreply@codium.team"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Welcome to Codium");
        String msg = "This is the confirmation email";
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        // If a proper SMTP server is configured, this line could be uncommented
        // Transport.send(message);

        return user;
    }
}
