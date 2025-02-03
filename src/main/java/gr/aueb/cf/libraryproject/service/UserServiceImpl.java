package gr.aueb.cf.libraryproject.service;

import ch.qos.logback.classic.encoder.JsonEncoder;
import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.mapper.BookMapper;
import gr.aueb.cf.libraryproject.mapper.UserMapper;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.business.User;
import gr.aueb.cf.libraryproject.model.entity.BookEntity;
import gr.aueb.cf.libraryproject.model.entity.UserEntity;
import gr.aueb.cf.libraryproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserServiceImpl implements IUserService{

    private final UserRepository userRepository;

    private final IBookService bookService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, IBookService bookService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookService = bookService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User insertUser(User user) throws Exception {
        try {
            if (userRepository.findUserEntityByEmail(user.getEmail()) != null) {throw new Exception("Email already Exists " + user.getEmail());}
            if (userRepository.findUserEntityByUsername(user.getUsername()) != null) {throw new Exception("Username already Exists " + user.getUsername());}
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UserEntity savedUserEntity = userRepository.save(UserMapper.mapUserToUserEntity(user));
            return UserMapper.mapUserEntityToUser(savedUserEntity);
        } catch (Exception e) {
            log.error("Error in Insert User, " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Override
    public User updateUser(BigInteger userId, User user) throws EntityNotFoundException {
        try{
            UserEntity userToUpdate = userRepository.findUserEntityById(userId);
            if (userToUpdate == null) throw new EntityNotFoundException(UserEntity.class, userId);

            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setRole(user.getRole());

            UserEntity savedUserEntity = userRepository.save(userToUpdate);

            return UserMapper.mapUserEntityToUser(savedUserEntity);

        } catch (EntityNotFoundException e) {
            log.info("Entity with id: " + userId + " not found");
            throw e;
        }
    }

    @Transactional
    @Override
    public void deleteUser(BigInteger id) throws EntityNotFoundException {
        try{
            UserEntity userToDelete = userRepository.findUserEntityById(id);
            if (userToDelete == null) throw new EntityNotFoundException(UserEntity.class, id);
            userRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            log.info("Entity with id: " + id + " does not exist");
            throw e;
        }
    }

    @Override
    public List<User> getUsers() throws EntityNotFoundException {
        List<UserEntity> userEntities;
        List<User> users = new ArrayList<>();
        try {
            userEntities = userRepository.findAll();
            if (userEntities.isEmpty()) throw new EntityNotFoundException(UserEntity.class, BigInteger.valueOf(1));
            for (UserEntity userEntity : userEntities) {
                users.add(UserMapper.mapUserEntityToUser(userEntity));
            }

            return users;
        } catch (EntityNotFoundException e) {
            log.info("No users found");
            throw e;
        }
    }

    @Override
    public User getUserById(BigInteger id) throws EntityNotFoundException {
        UserEntity userEntity = new UserEntity();
        try {
            userEntity = userRepository.findUserEntityById(id);
            if (userEntity == null) throw new EntityNotFoundException(UserEntity.class, id);
            return UserMapper.mapUserEntityToUser(userEntity);

        } catch (EntityNotFoundException e) {
            log.info("User with id " + id + "not found");
            throw e;
        }
    }

    @Override
    public User getUserByUsername(String username) throws EntityNotFoundException {
        UserEntity userEntity = new UserEntity();
        try {
            userEntity = userRepository.findUserEntityByUsername(username);
            if (userEntity == null) throw new EntityNotFoundException(UserEntity.class, null);
            return UserMapper.mapUserEntityToUser(userEntity);
        } catch (EntityNotFoundException e) {
            log.info("User with username " + username + "not found");
            throw e;
        }
    }

    @Override
    public User getUserByEmail(String email) throws EntityNotFoundException {
        UserEntity userEntity = new UserEntity();
        try {
            userEntity = userRepository.findUserEntityByEmail(email);
            if (userEntity == null)
                throw new EntityNotFoundException(UserEntity.class, null);
            return UserMapper.mapUserEntityToUser(userEntity);
        } catch (EntityNotFoundException e) {
            log.info("User with email " + email + "not found");
            throw e;

        }
    }

    @Transactional
    @Override
    public void addBook(BigInteger userId, BigInteger bookId) throws Exception {
        try {
            UserEntity userEntity = userRepository.findUserEntityById(userId);
//            User user = UserMapper.mapUserEntityToUser(userEntity);
            if  (userEntity == null) throw new Exception("User with id " + userId + "does not exist" );
            Book book = bookService.getBookById(bookId);
            BookEntity bookEntity = BookMapper.mapBookToBookEntity(book);
            if (bookEntity == null) throw new Exception("Book with id " + bookId + "doesn't exist");

            if (bookEntity.getQuantity() < 1) throw new Exception("Book with id: " + bookId + " not available");

            userEntity.getBooks().add(BookMapper.mapBookToBookEntity(book));
            userRepository.save(userEntity);

            bookEntity.setQuantity(bookEntity.getQuantity() - 1);
//            bookEntity.getUsers().add(userEntity);
            bookService.updateBook(bookId, BookMapper.mapBookEntityToBook(bookEntity));
        } catch (Exception e) {
            log.info("Error" + e.getMessage());
            throw e;
        }
    }

    @Transactional
    @Override
    public void removeBook(BigInteger userId, BigInteger bookId) throws Exception {
        try {
            UserEntity userEntity = userRepository.findUserEntityById(userId);
            if (userEntity == null) throw new EntityNotFoundException(UserEntity.class, userId);

            Book book = bookService.getBookById(bookId);
            BookEntity bookEntity = BookMapper.mapBookToBookEntity(book);
            if (bookEntity == null) throw new EntityNotFoundException(Book.class, bookId);

            userEntity.getBooks().remove(bookEntity);
//            bookEntity.getUsers().remove(userEntity);

            bookEntity.setQuantity(bookEntity.getQuantity() + 1);

            userRepository.save(userEntity);
            bookService.updateBook(bookId, BookMapper.mapBookEntityToBook(bookEntity));
        } catch (Exception e) {
            log.info("Error " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean isEmailAvailable(String email) throws Exception{
        try {
            UserEntity user = userRepository.getUserByEmail(email);
            return user == null;
        } catch (Exception e) {
            log.info("Error " + e.getMessage());
            throw e;
        }
    }
}
