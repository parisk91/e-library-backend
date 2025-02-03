package gr.aueb.cf.libraryproject.service;

import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.model.business.Author;

import java.math.BigInteger;
import java.util.List;

public interface IAuthorService {
    Author insertAuthor(Author author) throws Exception;
    Author updateAuthor(BigInteger authorId, Author author) throws EntityNotFoundException;
    void deleteAuthor(BigInteger id) throws EntityNotFoundException;
    List<Author> getAuthors();
    List<Author> getAuthorByLastname(String lastname) throws EntityNotFoundException;
    Author getAuthorById(BigInteger id) throws EntityNotFoundException;

    void addBookToAuthor(BigInteger authorId,BigInteger bookId) throws EntityNotFoundException;
    void removeBookFromAuthor(BigInteger authorId,BigInteger bookId) throws EntityNotFoundException;

}
