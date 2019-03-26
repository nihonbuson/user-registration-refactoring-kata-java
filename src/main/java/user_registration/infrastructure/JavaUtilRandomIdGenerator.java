package user_registration.infrastructure;

import user_registration.domain.RandomIdGenerator;

import java.util.Random;

public class JavaUtilRandomIdGenerator implements RandomIdGenerator {
    @Override
    public int generateId() {
        return new Random().nextInt();
    }
}
