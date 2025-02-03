package gr.aueb.cf.libraryproject.controller;

import gr.aueb.cf.libraryproject.dto.request.AuthorUpdateRequestDto;
import gr.aueb.cf.libraryproject.dto.request.BookInsertRequestDto;
import gr.aueb.cf.libraryproject.dto.request.BookUpdateRequestDto;
import gr.aueb.cf.libraryproject.dto.response.AuthorResponseDto;
import gr.aueb.cf.libraryproject.dto.response.BookResponseDto;
import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.mapper.AuthorMapper;
import gr.aueb.cf.libraryproject.mapper.BookMapper;
import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.service.IBookService;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "API for managing books in the library system.")
public class BookController {

    private final IBookService bookService;
    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Add a new book", description = "Creates a new book in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully.",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    @PostMapping
    public ResponseEntity<BookResponseDto> addBook(
            @RequestBody @Schema(description = "Details of the book to be added.") BookInsertRequestDto bookInsertRequestDto) {
        Book book = BookMapper.mapBookInsertRequestDtoToBook(bookInsertRequestDto);
        try {
            Book bookSaved = bookService.insertBook(book);
            BookResponseDto bookResponseDto  = BookMapper.mapBookToBookResponseDto(bookSaved);
            return new ResponseEntity<>(bookResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update an existing book", description = "Updates the details of a book by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully.",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> updateBook(
            @Parameter(description = "ID of the book to be updated.") @PathVariable("bookId") BigInteger bookId,
            @RequestBody @Schema(description = "Updated details of the book.") BookUpdateRequestDto bookUpdateRequestDto
    ){
        Book book = BookMapper.mapBookUpdateRequestDtoToBook(bookUpdateRequestDto);
        try{
            Book bookUpdated = bookService.updateBook(bookId, book);
            BookResponseDto bookResponseDto = BookMapper.mapBookToBookResponseDto(bookUpdated);
            return new ResponseEntity<>(bookResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a book", description = "Deletes a book by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to be deleted.") @PathVariable("bookId") BigInteger bookId) {
        try {
            bookService.deleteBook(bookId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all books", description = "Fetches a list of all books in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of books retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No books found.")
    })
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getBooks() {
        try {
            List<BookResponseDto> bookResponseDto = bookService.getAllBooks()
                    .stream()
                    .map(BookMapper::mapBookToBookResponseDto)
                    .toList();
            return new ResponseEntity<>(bookResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a book by ID", description = "Fetches the details of a book by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBooksById(
            @Parameter(description = "ID of the book to fetch.") @PathVariable("id") BigInteger id) {
        try {
            BookResponseDto bookResponseDto = BookMapper.mapBookToBookResponseDto(bookService.getBookById(id));
            return new ResponseEntity<>(bookResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get books by author ID", description = "Fetches all books written by a specific author.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully.",
                    content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No books found for the given author.")
    })
    @GetMapping("/authors/{authorId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthorId(
            @Parameter(description = "ID of the author to fetch books for.") @PathVariable("authorId") BigInteger authorId) {
        try {
            List<BookResponseDto> bookResponseDto = bookService.getBooksByAuthorId(authorId)
                    .stream().map(BookMapper::mapBookToBookResponseDto)
                    .toList();
            return new ResponseEntity<>(bookResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
