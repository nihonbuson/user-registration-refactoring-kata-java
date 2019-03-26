package user_registration.infrastructure;

import java.util.Random;

public class JavaUtilRandomIdGenerator {
    public int generateId() {
        return new Random().nextInt();
    }
}
