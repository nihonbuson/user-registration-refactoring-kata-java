package user_registration.domain;

public class InvalidEmailException extends Exception{
    public InvalidEmailException(Exception e) {
        super(e);
    }
}
