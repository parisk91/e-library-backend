package gr.aueb.cf.libraryproject.exceptions;

import java.math.BigInteger;

public class EntityAlreadyExistsException extends Exception{

    private static final BigInteger serialVersion = BigInteger.valueOf(1);

    public EntityAlreadyExistsException(Class<?> entityClass, BigInteger id) {
        super("Entity " + entityClass.getSimpleName() + "already exists");
    }
}
