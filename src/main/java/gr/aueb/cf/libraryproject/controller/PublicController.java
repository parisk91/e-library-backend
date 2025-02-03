package gr.aueb.cf.libraryproject.controller;

import gr.aueb.cf.libraryproject.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/public")
@Tag(name = "Public", description = "Public API for basic operations accessible without authentication.")
public class PublicController {
    private final IUserService userService;

    public PublicController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Check email availability",
            description = "Checks if an email address is available for registration. Returns a message indicating whether the email is in use or available."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email is available for registration.",
                    content = @Content(schema = @Schema(example = "{\"msg\": \"Email available\"}"))),
            @ApiResponse(responseCode = "400", description = "Email is already in use or an error occurred.",
                    content = @Content(schema = @Schema(example = "{\"msg\": \"Email already in use\"}")))
    })
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, String>> checkEmail(
            @Parameter(description = "The email address to check for availability.") @PathVariable("email") String email) {
        try {
            if (!userService.isEmailAvailable(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("msg", "Email already in use"));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("msg", "Email available"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("msg", e.getMessage()));
        }
    }
}
