package user_registration.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import user_registration.infrastructure.UserRegistrationController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;
import java.util.Random;

public class RegisterUser {
    public ResponseEntity execute(HttpServletRequest request, String password, String email) throws MessagingException {
        if (password.length() <= 8 || !password.contains("_")) {
            return new ResponseEntity("The password is not valid", HttpStatus.BAD_REQUEST);
        }

        if (UserRegistrationController.orm.findByEmail(email) != null) {
            return new ResponseEntity("The email is already in use", HttpStatus.BAD_REQUEST);
        }

        User user = new User(
                new Random().nextInt(),
                request.getParameter("name"),
                email,
                password
        );
        UserRegistrationController.orm.save(user);

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

        return ResponseEntity.ok(user);
    }
}
