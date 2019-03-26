package user_registration.domain;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.Random;

public class RegisterUser {

    private final UserRepository userRepository;

    public RegisterUser(UserRepository userOrmRepository) {
        userRepository = userOrmRepository;
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

        sendConfirmationEmail(email, "Welcome to Codium", "This is the confirmation email");

        return user;
    }

    private void sendConfirmationEmail(String email, String subject, String body) throws MessagingException {
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
        message.setSubject(subject);
        String msg = body;
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        // If a proper SMTP server is configured, this line could be uncommented
        // Transport.send(message);
    }
}
