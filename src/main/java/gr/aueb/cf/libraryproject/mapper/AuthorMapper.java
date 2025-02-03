package gr.aueb.cf.libraryproject.mapper;

import gr.aueb.cf.libraryproject.dto.request.AuthorInsertRequestDto;
import gr.aueb.cf.libraryproject.dto.request.AuthorUpdateRequestDto;
import gr.aueb.cf.libraryproject.dto.response.AuthorResponseDto;
import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.entity.AuthorEntity;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorMapper {

    //maps AuthorInsertRequestDto to Author
    public static Author mapAuthorInsertDtoToAuthor(AuthorInsertRequestDto authorInsertRequestDto) {
        return new Author (null, authorInsertRequestDto.getFirstname(), authorInsertRequestDto.getLastname(), authorInsertRequestDto.getBiography(), null);
    }

    //maps AuthorUpdateRequestDto to Author
    public static Author mapAuthorUpdateDtoToAuthor(AuthorUpdateRequestDto authorUpdateRequestDto) {
        return new Author (null, authorUpdateRequestDto.getFirstname(), authorUpdateRequestDto.getLastname(), authorUpdateRequestDto.getBiography(), null);
    }

    //maps Author to AuthorWithBooksResponseDto
    public static AuthorResponseDto mapAuthorToAuthorResponseDto(Author author) {
        AuthorResponseDto authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setId(author.getId());
        authorResponseDto.setFirstname(author.getFirstname());
        authorResponseDto.setLastname(author.getLastname());
        authorResponseDto.setBiography(author.getBiography());
        authorResponseDto.setBooks(author.getBooks());

        return authorResponseDto;
    }

    //maps Author to AuthorEntity and the inserted books too
    public static AuthorEntity mapAuthorToAuthorEntity(Author author) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(author.getId());
        authorEntity.setFirstname(author.getFirstname());
        authorEntity.setLastname(author.getLastname());
        authorEntity.setBiography(author.getBiography());

        return authorEntity;
    }

    //maps AuthorEntity to Author and the inserted books too
    public static Author mapAuthorEntityToAuthor(AuthorEntity authorEntity) {
        Author author = new Author();
        author.setId(authorEntity.getId());
        author.setFirstname(authorEntity.getFirstname());
        author.setLastname(authorEntity.getLastname());
        author.setBiography(authorEntity.getBiography());

        List<Book> books;
        books = authorEntity.getBooks().stream()
                .map(BookMapper::mapBookEntityToBookWithoutAuthor)
                .collect(Collectors.toList());

        author.setBooks(books);
        return author;
    }

    public static Author mapAuthorEntityToAuthorWithoutBooks(AuthorEntity authorEntity) {
        Author author = new Author();
        author.setId(authorEntity.getId());
        author.setFirstname(authorEntity.getFirstname());
        author.setLastname(authorEntity.getLastname());
        author.setBiography(authorEntity.getBiography());

        return author;
    }
}
