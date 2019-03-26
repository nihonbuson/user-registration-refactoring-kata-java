package user_registration.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import user_registration.domain.*;

import javax.mail.*;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UserRegistrationController {
    public static UserRepository orm = new UserOrmRepository();

    @PostMapping("/users")
    public ResponseEntity createUser(HttpServletRequest request) throws InvalidEmailException {
        try {
            User user = new RegisterUser(orm).execute(
                    request.getParameter("password"),
                    request.getParameter("email"),
                    request.getParameter("name")
            );
            return ResponseEntity.ok(user);
        } catch (PasswordIsNotValidException e) {
            return new ResponseEntity("The password is not valid", HttpStatus.BAD_REQUEST);
        } catch (EmailIsAlreadyInUseException e) {
            return new ResponseEntity("The email is already in use", HttpStatus.BAD_REQUEST);
        }
    }

}
