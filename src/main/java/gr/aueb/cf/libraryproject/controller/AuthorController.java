package gr.aueb.cf.libraryproject.controller;

import gr.aueb.cf.libraryproject.dto.request.AuthorInsertRequestDto;
import gr.aueb.cf.libraryproject.dto.request.AuthorUpdateRequestDto;
import gr.aueb.cf.libraryproject.dto.response.AuthorResponseDto;
import gr.aueb.cf.libraryproject.dto.response.BookResponseDto;
import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.mapper.AuthorMapper;
import gr.aueb.cf.libraryproject.mapper.BookMapper;
import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.service.IAuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
@Tag(name = "Authors", description = "API for managing authors in the library system.")
public class AuthorController {

    private final IAuthorService authorService;
    @Autowired
    public AuthorController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "Get all authors", description = "Fetches a list of all authors.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of authors retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No authors found.")
    })
    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAuthors() {
        try {
            List<AuthorResponseDto> authorResponseDto = authorService.getAuthors()
                    .stream()
                    .map(AuthorMapper::mapAuthorToAuthorResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(authorResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get an author by ID", description = "Fetches the details of an author by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Author not found.")
    })
    @GetMapping( "/{authorId}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(
            @Parameter(description = "ID of the author to fetch.") @PathVariable("authorId") BigInteger authorId) {
        try {
            Author author = authorService.getAuthorById(authorId);
            AuthorResponseDto authorResponseDto = AuthorMapper.mapAuthorToAuthorResponseDto(author);
            return new ResponseEntity<>(authorResponseDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get authors by lastname", description = "Fetches authors based on their last name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of authors retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    @GetMapping("/{lastname}")
    public ResponseEntity<List<AuthorResponseDto>> getAuthorByLastname(
            @Parameter(description = "Lastname of the author.") @PathVariable("lastname") String lastname) {
        List<Author> authors;
        try {
            authors = authorService.getAuthorByLastname(lastname);
            List<AuthorResponseDto> authorsDTO = new ArrayList<>();
            for (Author author : authors) {
                authorsDTO.add(AuthorMapper.mapAuthorToAuthorResponseDto(author));
            }
            return new ResponseEntity<>(authorsDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Add a new author", description = "Creates a new author in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author created successfully.",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    @PostMapping
    public ResponseEntity<AuthorResponseDto> addAuthor(
            @RequestBody @Schema(description = "Details of the author to be added.") AuthorInsertRequestDto authorInsertRequestDto) {
        Author author = AuthorMapper.mapAuthorInsertDtoToAuthor(authorInsertRequestDto);
        try {
            Author authorToSave = authorService.insertAuthor(author);
            AuthorResponseDto authorResponseDto = AuthorMapper.mapAuthorToAuthorResponseDto(authorToSave);
            return new ResponseEntity<>(authorResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update an author", description = "Updates the details of an author by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author updated successfully.",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Author not found.")
    })
    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(
            @Parameter(description = "ID of the author to update.") @PathVariable("authorId") BigInteger authorId,
            @RequestBody @Schema(description = "Updated details of the author.") AuthorUpdateRequestDto authorUpdateRequestDto
    ){
        Author author = AuthorMapper.mapAuthorUpdateDtoToAuthor(authorUpdateRequestDto);
        try{
            Author authorUpdated = authorService.updateAuthor(authorId, author);
            AuthorResponseDto authorResponseDto = AuthorMapper.mapAuthorToAuthorResponseDto(authorUpdated);
            return new ResponseEntity<>(authorResponseDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete an author", description = "Deletes an author by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Author not found.")
    })
    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "ID of the author to delete.") @PathVariable("authorId") BigInteger authorId) {
        try {
            authorService.deleteAuthor(authorId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Adds a book to an author", description = "Associates a book with an author by their IDs.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book added to author successfully.",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Author or book not found.")
    })
    @PutMapping("/{authorId}/authors/{bookId}")
    public ResponseEntity<AuthorResponseDto> addBookToAuthorId(
            @Parameter(description = "ID of the author.") @PathVariable("authorId") BigInteger authorId,
            @Parameter(description = "ID of the book.") @PathVariable("bookId") BigInteger bookId) {
        try {
            authorService.addBookToAuthor(authorId, bookId);
            Author author = authorService.getAuthorById(authorId);
            AuthorResponseDto authorResponseDto = AuthorMapper.mapAuthorToAuthorResponseDto(author);
            return new ResponseEntity<>(authorResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Remove a book from an author", description = "Disassociates a book from an author by their IDs.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book removed from author successfully.",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Author or book not found.")
    })
    @DeleteMapping("/{authorId}/books/{bookId}")
    public ResponseEntity<AuthorResponseDto> removeBookToAuthorId(
            @Parameter(description = "ID of the author.") @PathVariable("authorId") BigInteger authorId,
            @Parameter(description = "ID of the book.") @PathVariable("bookId") BigInteger bookId) {
        try {
            authorService.removeBookFromAuthor(authorId, bookId);
            Author author = authorService.getAuthorById(authorId);
            AuthorResponseDto authorResponseDto = AuthorMapper.mapAuthorToAuthorResponseDto(author);
            return new ResponseEntity<>(authorResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
