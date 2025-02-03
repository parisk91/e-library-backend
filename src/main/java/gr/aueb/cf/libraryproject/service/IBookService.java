package gr.aueb.cf.libraryproject.service;

import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.entity.AuthorEntity;

import java.math.BigInteger;
import java.util.List;

public interface IBookService {
    Book insertBook(Book book) throws Exception;
    Book updateBook(BigInteger bookId, Book book) throws Exception;
    void deleteBook(BigInteger id) throws EntityNotFoundException;
    void deleteBooksByAuthorId(BigInteger authorId) throws EntityNotFoundException;
    List<Book> getAllBooks() throws Exception;
    Book getBookById(BigInteger id) throws EntityNotFoundException;
    List<Book> getBooksByAuthorId(BigInteger id) throws EntityNotFoundException;
}
