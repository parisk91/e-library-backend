package gr.aueb.cf.libraryproject.controller;

import gr.aueb.cf.libraryproject.dto.request.UserInsertRequestDto;
import gr.aueb.cf.libraryproject.dto.request.UserUpdateRequestDto;
import gr.aueb.cf.libraryproject.dto.response.UserResponseDto;
import gr.aueb.cf.libraryproject.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryproject.mapper.UserMapper;
import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.business.User;
import gr.aueb.cf.libraryproject.service.IBookService;
import gr.aueb.cf.libraryproject.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "API for managing users.")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add a new user", description = "Create a new user in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> addUser(@RequestBody UserInsertRequestDto userInsertRequestDto) {
        try {
            User user = UserMapper.mapUserInsertRequestDtoToUser(userInsertRequestDto);
            User userToSave = userService.insertUser(user);
            UserResponseDto userResponseDto = UserMapper.mapUserToUserResponseDto(userToSave);
            return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update user", description = "Update an existing user's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully updated."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "ID of the user to update.", example = "12345") @PathVariable("userId") BigInteger userId,
            @RequestBody UserUpdateRequestDto userUpdateRequestDto
    ) {
        User user = UserMapper.mapUserUpdateRequestDtoToUser(userUpdateRequestDto);
        try {
            User userUpdated = userService.updateUser(userId, user);
            UserResponseDto userResponseDto = UserMapper.mapUserToUserResponseDto(userUpdated);
            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete user", description = "Delete a user by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully deleted."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete.") @PathVariable("userId") BigInteger userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No users found.")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        try {
            List<UserResponseDto> userResponseDto = userService.getUsers()
                    .stream()
                    .map(UserMapper::mapUserToUserResponseDto)
                    .toList();
            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "ID of the user to retrieve.") @PathVariable("userId") BigInteger userId) {
        User user;
        try {
            user = userService.getUserById(userId);
            UserResponseDto userResponseDto = UserMapper.mapUserToUserResponseDto(user);
            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get user by username", description = "Retrieve a user by their username.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(
            @Parameter(description = "Username of the user to retrieve.") @PathVariable("username") String username) {
        User user;
        try {
            user = userService.getUserByUsername(username);
            UserResponseDto userResponseDto = UserMapper.mapUserToUserResponseDto(user);
            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get user by email", description = "Retrieve a user by their email address.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @Parameter(description = "Email of the user to retrieve.") @PathVariable("email") String email) {
        User user;
        try {
            user = userService.getUserByEmail(email);
            UserResponseDto userResponseDto = UserMapper.mapUserToUserResponseDto(user);
            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a book to a user", description = "Assign a book to a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book successfully added to the user."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @PutMapping("/{userId}/addBooks/{bookId}")
    public ResponseEntity<Void> addBookToUser(
            @Parameter(description = "ID of the user.") @PathVariable("userId") BigInteger userId,
            @Parameter(description = "ID of the book to add.") @PathVariable("bookId") BigInteger bookId) {
        try {
            userService.addBook(userId, bookId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Remove a book from a user", description = "Unassign a book from a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book successfully removed from the user."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
        @DeleteMapping("/{userId}/removeBooks/{bookId}")
    public ResponseEntity<Void> removeBook(
            @Parameter(description = "ID of the user.") @PathVariable("userId") BigInteger userId,
            @PathVariable("bookId") BigInteger bookId) {
        try {
            userService.removeBook(userId, bookId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get user profile", description = "Retrieve the currently authenticated user's profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "User is not authenticated.")
    })
    @Transactional
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return ResponseEntity.ok(userDetails);
        };
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user details in context");
    }
}

