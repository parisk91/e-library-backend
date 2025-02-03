package gr.aueb.cf.libraryproject.controller;

import gr.aueb.cf.libraryproject.authentication.AuthenticationService;
import gr.aueb.cf.libraryproject.dto.request.AuthenticationRequestDto;
import gr.aueb.cf.libraryproject.dto.request.UserInsertRequestDto;
import gr.aueb.cf.libraryproject.dto.response.AuthenticationResponseDto;
import gr.aueb.cf.libraryproject.dto.response.UserResponseDto;
import gr.aueb.cf.libraryproject.mapper.UserMapper;
import gr.aueb.cf.libraryproject.model.business.User;
import gr.aueb.cf.libraryproject.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for user authentication and registration.")
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);
    private final AuthenticationService authenticationService;
    private final IUserService userService;

    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns an access token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully.",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed.",
                    content = @Content(schema = @Schema(example = "Unauthorized"))),
            @ApiResponse(responseCode = "400", description = "Bad request.")
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody @Schema(description = "User credentials for authentication.") AuthenticationRequestDto authenticationRequestDto)
            throws Exception {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.authenticate(authenticationRequestDto);
        LOGGER.info("User authenticated");
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + authenticationResponseDto.getAccess_token())
                .body(authenticationResponseDto);
    }

    @Operation(summary = "Sign up a new user", description = "Registers a new user and returns the created user's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User signed up successfully.",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(schema = @Schema(example = "Bad Request")))
    })
    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> signUp(
            @Schema(description = "Details of the user to sign up.") @RequestBody UserInsertRequestDto userInsertRequestDto) {
        try {
            User user= UserMapper.mapUserInsertRequestDtoToUser(userInsertRequestDto);
            User userInserted = userService.insertUser(user);
            UserResponseDto userResponseDto = UserMapper.mapUserToUserResponseDto(userInserted);

            LOGGER.info("User signed up with username: " + userInsertRequestDto.getUsername());
            return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error during user sign-up: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}

