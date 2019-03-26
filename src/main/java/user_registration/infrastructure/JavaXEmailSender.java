package user_registration.infrastructure;

import user_registration.domain.Email;
import user_registration.domain.EmailSender;
import user_registration.domain.EmailException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class JavaXEmailSender implements EmailSender {
    @Override
    public void send(Email theEmail) throws EmailException {
        send(theEmail.getEmailAddress(), theEmail.getSubject(), theEmail.getBody());
    }

    public void send(String email, String subject, String body) throws EmailException {
        Properties prop = new Properties();
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("smtpUsername", "smtpPassword");
            }
        });
        try {
            Message message = prepareEmail(email, subject, body, session);
            // If a proper SMTP server is configured, this line could be uncommented
            // Transport.send(message);
        } catch (MessagingException e) {
            throw new EmailException(e);
        }
    }

    private Message prepareEmail(String email, String subject, String body, Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("noreply@codium.team"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject(subject);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(body, "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);

        return message;
    }
}
