package user_registration.domain;

public interface EmailSender {
    void send(Email theEmail) throws EmailException;
}
