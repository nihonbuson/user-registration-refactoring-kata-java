package user_registration.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import user_registration.domain.RegisterUser;

import javax.mail.*;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UserRegistrationController {
    public static UserOrmRepository orm = new UserOrmRepository();

    @PostMapping("/users")
    public ResponseEntity createUser(HttpServletRequest request) throws MessagingException {
        return new RegisterUser().execute(request, request.getParameter("password"));
    }

}
