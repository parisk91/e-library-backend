package gr.aueb.cf.libraryproject.mapper;

import gr.aueb.cf.libraryproject.dto.request.BookInsertRequestDto;
import gr.aueb.cf.libraryproject.dto.request.BookUpdateRequestDto;
import gr.aueb.cf.libraryproject.dto.response.BookResponseDto;
import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.business.User;
import gr.aueb.cf.libraryproject.model.entity.BookEntity;
import gr.aueb.cf.libraryproject.model.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class BookMapper {

    public static Book mapBookInsertRequestDtoToBook(BookInsertRequestDto bookInsertRequestDto) {
        Book book = new Book();
        book.setId(null);
        book.setTitle(bookInsertRequestDto.getTitle());
        book.setDescription(bookInsertRequestDto.getDescription());
        book.setQuantity(bookInsertRequestDto.getQuantity());
        book.setAuthor(new Author(bookInsertRequestDto.getAuthorId(), null, null, null, null));

        return book;
    }

    public static Book mapBookUpdateRequestDtoToBook(BookUpdateRequestDto bookUpdateRequestDto) {
        Book book = new Book();
        book.setId(book.getId());
        book.setTitle(bookUpdateRequestDto.getTitle());
        book.setDescription(bookUpdateRequestDto.getDescription());
        book.setQuantity(bookUpdateRequestDto.getQuantity());
        book.setAuthor(new Author(bookUpdateRequestDto.getAuthorId(), null, null, null, null));

        return book;
    }

    public static BookResponseDto mapBookToBookResponseDto(Book book) {

        return new BookResponseDto(book.getId(), book.getTitle(), book.getDescription(), book.getQuantity(), book.getAuthor());
    }

    public static BookEntity mapBookToBookEntity(Book book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(book.getId());
        bookEntity.setTitle(book.getTitle());
        bookEntity.setDescription(book.getDescription());
        bookEntity.setQuantity(book.getQuantity());
        bookEntity.setAuthor(AuthorMapper.mapAuthorToAuthorEntity(book.getAuthor()));

//        List<UserEntity> users = new ArrayList<>();
//        if (book.getUsers() != null) {
//            users = book.getUsers().stream()
//                    .map(UserMapper::mapUserToUserEntityWithoutBooks)
//                    .toList();
//        }
//        for (UserEntity user : users) {
//            bookEntity.getUsers().add(user);
//        }
        return bookEntity;
    }

    public static Book mapBookEntityToBook(BookEntity bookEntity) {
        Book book = new Book();
        book.setId(bookEntity.getId());
        book.setTitle(bookEntity.getTitle());
        book.setDescription(bookEntity.getDescription());
        book.setQuantity(bookEntity.getQuantity());
        book.setAuthor(AuthorMapper.mapAuthorEntityToAuthorWithoutBooks(bookEntity.getAuthor()));

        List<User> users = new ArrayList<>();
//        if (book.getUsers() != null) {
//            users = bookEntity.getUsers().stream()
//                    .map(UserMapper::mapUserEntityToUserWithoutBooks)
//                    .toList();
//        }
//        for (User user : users) {
//            book.getUsers().add(user);
//        }

        return book;
    }

    public static Book mapBookEntityToBookWithoutAuthor(BookEntity bookEntity) {
        Book book = new Book();
        book.setId(bookEntity.getId());
        book.setTitle(bookEntity.getTitle());
        book.setDescription(bookEntity.getDescription());
        book.setQuantity(bookEntity.getQuantity());

        List<User> users = new ArrayList<>();
//        if (book.getUsers() != null) {
//            users = bookEntity.getUsers().stream()
//                    .map(UserMapper::mapUserEntityToUser)
//                    .toList();
//        }
//        for (User user : users) {
//            book.getUsers().add(user);
//        }

        return book;
    }
}
