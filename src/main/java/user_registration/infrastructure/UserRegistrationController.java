package user_registration.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import user_registration.domain.User;
import user_registration.infrastructure.UserOrmRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;
import java.util.Random;

@RestController
public class UserRegistrationController {
    public static UserOrmRepository orm = new UserOrmRepository();

    @PostMapping("/users")
    public ResponseEntity createUser(HttpServletRequest request) throws MessagingException {

        if (request.getParameter("password").length() <= 8 || !request.getParameter("password").contains("_")) {
            return new ResponseEntity("The password is not valid", HttpStatus.BAD_REQUEST);
        }

        if (orm.findByEmail(request.getParameter("email")) != null) {
            return new ResponseEntity("The email is already in use", HttpStatus.BAD_REQUEST);
        }

        User user = new User(
                new Random().nextInt(),
                request.getParameter("name"),
                request.getParameter("email"),
                request.getParameter("password")
        );
        orm.save(user);

        Properties prop = new Properties();
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("smtpUsername", "smtpPassword");
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("noreply@codium.team"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(request.getParameter("email")));
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
