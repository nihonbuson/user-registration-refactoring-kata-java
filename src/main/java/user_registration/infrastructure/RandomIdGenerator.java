package user_registration.infrastructure;

import java.util.Random;

public class RandomIdGenerator {
    public int generateId() {
        return new Random().nextInt();
    }
}
