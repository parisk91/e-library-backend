package gr.aueb.cf.libraryproject.service;

import gr.aueb.cf.libraryproject.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.business.User;

import java.math.BigInteger;
import java.util.List;

public interface IUserService {

    User insertUser(User user) throws Exception;
    User updateUser(BigInteger userIId, User user) throws EntityNotFoundException;
    void deleteUser(BigInteger id) throws EntityNotFoundException;
    List<User> getUsers() throws EntityNotFoundException;
    User getUserById(BigInteger id) throws EntityNotFoundException;
    User getUserByUsername(String username) throws EntityNotFoundException;
    User getUserByEmail(String email) throws EntityNotFoundException;
    void addBook(BigInteger userId, BigInteger bookId) throws Exception;
    void removeBook(BigInteger userId, BigInteger bookId) throws Exception;
    boolean isEmailAvailable(String email) throws Exception;

}
