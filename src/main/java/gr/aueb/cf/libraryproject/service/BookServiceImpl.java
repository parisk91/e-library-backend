package gr.aueb.cf.libraryproject.service;

import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.mapper.AuthorMapper;
import gr.aueb.cf.libraryproject.mapper.BookMapper;
import gr.aueb.cf.libraryproject.mapper.UserMapper;
import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.business.User;
import gr.aueb.cf.libraryproject.model.entity.AuthorEntity;
import gr.aueb.cf.libraryproject.model.entity.BookEntity;
import gr.aueb.cf.libraryproject.model.entity.UserEntity;
import gr.aueb.cf.libraryproject.repository.AuthorRepository;
import gr.aueb.cf.libraryproject.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements IBookService {

    private final BookRepository bookRepository;
    private final IAuthorService authorService;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, IAuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Transactional
    @Override
    public Book insertBook(Book book) throws Exception {

        try {
            BookEntity bookEntity = BookMapper.mapBookToBookEntity(book);
            if (book.getAuthor() == null || book.getAuthor().getId() == null) {
                throw new Exception("Error in author id");
            }
            if (authorService.getAuthorById(book.getAuthor().getId()) == null)
                throw new Exception("No author with this id found");
            if (bookRepository.findBookEntityByTitleAndAuthor(book.getTitle(), AuthorMapper.mapAuthorToAuthorEntity(book.getAuthor())) != null)
                throw new Exception("Book already exists");
            BookEntity bookSaved = bookRepository.save(bookEntity);

            return BookMapper.mapBookEntityToBook(bookSaved);
        } catch (Exception e) {
            log.info("Insert " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Override
    public Book updateBook(BigInteger bookId, Book book) throws EntityNotFoundException{
        try {
            BookEntity bookToUpdate = bookRepository.findBookEntityById(bookId);
            Author author = authorService.getAuthorById(book.getAuthor().getId());
            if (bookToUpdate == null) throw new EntityNotFoundException(BookEntity.class, bookId);
            if (author == null) throw new EntityNotFoundException(Author.class, book.getAuthor().getId());

            bookToUpdate.setTitle(book.getTitle());
            bookToUpdate.setDescription(book.getDescription());
            bookToUpdate.setQuantity(book.getQuantity());
            bookToUpdate.setAuthor(AuthorMapper.mapAuthorToAuthorEntity(book.getAuthor()));

            BookEntity savedBookEntity = bookRepository.save(bookToUpdate);

            return BookMapper.mapBookEntityToBook(savedBookEntity) ;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteBook(BigInteger bookId) throws EntityNotFoundException {
        try {
            if (bookRepository.findBookEntityById(bookId) == null) throw new EntityNotFoundException(BookEntity.class, bookId);
            bookRepository.deleteById(bookId);
        } catch (EntityNotFoundException e) {
            log.info("Error in delete book, no book with id:" + bookId);
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteBooksByAuthorId(BigInteger authorId) throws EntityNotFoundException {
            try {
                if (bookRepository.findBookEntitiesByAuthorId(authorId) == null) throw new EntityNotFoundException(AuthorEntity.class, authorId);
                bookRepository.deleteBookEntitiesByAuthorId(authorId);
            } catch (EntityNotFoundException e) {
                log.info("No books for author with id:" +authorId);
                throw e;
            }
    }

    @Override
    public List<Book> getAllBooks() throws Exception {
        try {
            List<BookEntity> booksEntity = bookRepository.findAll();
            if (booksEntity.isEmpty()) throw new Exception("No books found");
            List<Book> books = booksEntity.stream()
                    .map(BookMapper::mapBookEntityToBook)
                    .collect(Collectors.toList());
            System.out.println(books);
            return books;
        } catch (Exception e) {
            log.info("No books found");
            throw e;
        }
    }

    @Override
    public Book getBookById(BigInteger id) throws EntityNotFoundException {
        try {
            BookEntity bookEntity = bookRepository.findBookEntityById(id);
            if (bookEntity == null) throw new EntityNotFoundException(BookEntity.class, id);

            Book book = BookMapper.mapBookEntityToBook(bookEntity);
            return book;
        } catch (EntityNotFoundException e) {
            log.info("No books found for this id" + id);
            throw e;
        }
    }

    @Override
    public List<Book> getBooksByAuthorId(BigInteger authorId) throws EntityNotFoundException {
        List<Book> books = new ArrayList<>();
        try {
            List<BookEntity> booksEntity = bookRepository.findBookEntitiesByAuthorId(authorId);
            if (booksEntity.isEmpty()) throw new EntityNotFoundException(BookEntity.class, authorId);
            for (BookEntity book : booksEntity) {
                books.add(BookMapper.mapBookEntityToBook(book));
            }
        } catch (EntityNotFoundException e) {
            log.info("No books found for this author id");
            throw e;
        }
        return books;
    }

}
