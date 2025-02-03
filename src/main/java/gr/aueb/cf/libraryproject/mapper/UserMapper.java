package gr.aueb.cf.libraryproject.mapper;

import gr.aueb.cf.libraryproject.dto.request.UserInsertRequestDto;
import gr.aueb.cf.libraryproject.dto.request.UserUpdateRequestDto;
import gr.aueb.cf.libraryproject.dto.response.AuthorResponseDto;
import gr.aueb.cf.libraryproject.dto.response.UserResponseDto;
import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.business.User;
import gr.aueb.cf.libraryproject.model.entity.AuthorEntity;
import gr.aueb.cf.libraryproject.model.entity.BookEntity;
import gr.aueb.cf.libraryproject.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {



//    private static PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public UserMapper(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    public static User mapUserInsertRequestDtoToUser(UserInsertRequestDto userInsertRequestDto) {
        return new User (null, userInsertRequestDto.getUsername(), userInsertRequestDto.getPassword(), userInsertRequestDto.getEmail(), userInsertRequestDto.getRole(),null, null);
    }

    public static User mapUserUpdateRequestDtoToUser(UserUpdateRequestDto userUpdateRequestDto) {
        return new User (null, userUpdateRequestDto.getUsername(), userUpdateRequestDto.getPassword(), userUpdateRequestDto.getEmail(),userUpdateRequestDto.getRole(),null,  null );
    }

    public static UserResponseDto mapUserToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
//        userResponseDto.setPassword(user.getPassword());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setBooks(user.getBooks());

        return userResponseDto;
    }

    public static UserEntity mapUserToUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(user.getRole());
        userEntity.setEmail(user.getEmail());

        List<BookEntity> books = new ArrayList<>();
        if (user.getBooks() != null) {
            books = user.getBooks().stream()
                    .map(BookMapper::mapBookToBookEntity)
                    .toList();
        }

        userEntity.setBooks(books);
        return userEntity;
    }

    public static User mapUserEntityToUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setPassword(userEntity.getPassword());
        user.setEmail(userEntity.getEmail());
        user.setRole(userEntity.getRole());

        List<Book> books = new ArrayList<>();
        if (userEntity.getBooks() != null) {
            books = userEntity.getBooks().stream()
                    .map(BookMapper::mapBookEntityToBook)
                    .toList();
        }
        user.setBooks(books);
        return user;
    }

//    public static UserEntity mapUserToUserEntityWithoutBooks(User user) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(user.getId());
//        userEntity.setUsername(user.getUsername());
//        userEntity.setPassword(user.getPassword());
//        userEntity.setRole(user.getRole());
//        userEntity.setEmail(user.getEmail());
//        userEntity.setBooks(null);
//        return userEntity;
//    }

//    public static User mapUserEntityToUserWithoutBooks(UserEntity userEntity) {
//        User user = new User();
//        user.setId(userEntity.getId());
//        user.setUsername(userEntity.getUsername());
//        user.setPassword(userEntity.getPassword());
//        user.setEmail(userEntity.getEmail());
//        user.setRole(userEntity.getRole());
//        user.setBooks(null);
//
//        return user;
//    }
}
