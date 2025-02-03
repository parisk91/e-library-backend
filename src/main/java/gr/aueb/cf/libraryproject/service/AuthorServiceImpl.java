package gr.aueb.cf.libraryproject.service;

import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.mapper.AuthorMapper;
import gr.aueb.cf.libraryproject.mapper.BookMapper;
import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.model.entity.AuthorEntity;
import gr.aueb.cf.libraryproject.model.entity.BookEntity;
import gr.aueb.cf.libraryproject.repository.AuthorRepository;
import gr.aueb.cf.libraryproject.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AuthorServiceImpl implements IAuthorService{

    private final AuthorRepository authorRepository;
    private final IBookService bookService;
    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository,@Lazy IBookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
    }

    @Transactional
    @Override
    public Author insertAuthor(Author author) throws Exception {
        try {
            AuthorEntity authorToSave = AuthorMapper.mapAuthorToAuthorEntity(author);
            if (authorRepository.findByFirstnameAndLastname(authorToSave.getFirstname(), authorToSave.getLastname()).isPresent()) {
                throw new Exception("Author already exists");}
            AuthorEntity savedAuthor = authorRepository.save(authorToSave);
            if (savedAuthor.getId() == null) {
                throw new Exception("Author not created");}
            return AuthorMapper.mapAuthorEntityToAuthor(savedAuthor);
        } catch (Exception e) {
            log.error("Error in Author Insert " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Override
    public Author updateAuthor(BigInteger authorId, Author author) throws EntityNotFoundException {
       try {
           AuthorEntity authorToUpdate = authorRepository.findAuthorById(authorId);
           if (authorToUpdate == null) throw new EntityNotFoundException(AuthorEntity.class, authorId);

           authorToUpdate.setFirstname(author.getFirstname());
           authorToUpdate.setLastname(author.getLastname());
           authorToUpdate.setBiography(author.getBiography());

           AuthorEntity savedAuthorEntity = authorRepository.save(authorToUpdate);

           return AuthorMapper.mapAuthorEntityToAuthor(savedAuthorEntity) ;
       } catch (EntityNotFoundException e) {
           log.info("Author with id " + authorId + " does not exist");
           throw e;
       }
    }

    @Transactional
    @Override
    public void deleteAuthor(BigInteger authorId) throws EntityNotFoundException {
        try {
            if (authorRepository.findAuthorById(authorId) == null) throw new EntityNotFoundException(AuthorEntity.class, authorId);
            bookService.deleteBooksByAuthorId(authorId);
            authorRepository.deleteById(authorId);
        } catch (EntityNotFoundException e) {
            log.info("Error in delete author");
            throw e;
        }
    }

    @Override
    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        try {
            List<AuthorEntity> authorEntities;
            authorEntities = authorRepository.findAll();
            for (AuthorEntity authorEntity : authorEntities) {
                authors.add(AuthorMapper.mapAuthorEntityToAuthor(authorEntity));
            }
        } catch (Exception e) {
            log.info("Error in Get Authors");
            throw e;
        }
        return authors;
    }

    @Override
    public List<Author> getAuthorByLastname(String lastname) throws EntityNotFoundException {
        List<AuthorEntity> authorEntities;
        List<Author> authors = new ArrayList<>();
        try {
            authorEntities = authorRepository.findAuthorByLastname(lastname);
            if (authorEntities.isEmpty()) throw new EntityNotFoundException(Author.class , BigInteger.valueOf(1L));
            for (AuthorEntity author : authorEntities) {
                authors.add(AuthorMapper.mapAuthorEntityToAuthor(author));
            }
        } catch (EntityNotFoundException e) {
            log.info("Error in get authors by lastname");
            throw  e;
        }
        return authors;
    }

    @Override
    public Author getAuthorById(BigInteger id) throws EntityNotFoundException {
        try {
            AuthorEntity authorEntity = authorRepository.findAuthorById(id);
            if (authorEntity == null) throw new EntityNotFoundException(AuthorEntity.class, id);
            return AuthorMapper.mapAuthorEntityToAuthor(authorEntity);
        } catch (EntityNotFoundException e) {
            log.info("Error in Get Author by Id. Author with id: " + id + " does not exist");
            throw e;
        }
    }

    @Override
    public void addBookToAuthor(BigInteger authorId, BigInteger bookId) throws EntityNotFoundException {
        try {
            AuthorEntity authorEntity = authorRepository.findAuthorById(authorId);
            BookEntity bookEntity = BookMapper.mapBookToBookEntity(bookService.getBookById(bookId));

            authorEntity.getBooks().add(bookEntity);
            authorRepository.save(authorEntity);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeBookFromAuthor(BigInteger authorId, BigInteger bookId) throws EntityNotFoundException {
        try {
            AuthorEntity authorEntity = authorRepository.findAuthorById(authorId);
            BookEntity bookEntity = BookMapper.mapBookToBookEntity(bookService.getBookById(bookId));

            if(authorEntity.getBooks().contains(bookEntity)) throw new  EntityNotFoundException(BookEntity.class, bookId);
            authorEntity.getBooks().remove(bookEntity);
            authorRepository.save(authorEntity);
        } catch (EntityNotFoundException e) {
            log.info("Error in Remove Book from author. Author does not contain book with id: " + bookId);
            throw e;
        }
    }
}
