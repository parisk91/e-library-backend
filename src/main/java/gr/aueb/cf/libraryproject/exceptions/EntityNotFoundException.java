package gr.aueb.cf.libraryproject.exceptions;

import java.math.BigInteger;

public class EntityNotFoundException extends Exception{
    private static final BigInteger serialVersion = BigInteger.valueOf(1);

    public EntityNotFoundException(Class<?> entityClass, BigInteger id) {
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " does not exist");
    }
}
