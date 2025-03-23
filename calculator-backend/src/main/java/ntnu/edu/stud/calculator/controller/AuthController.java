package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.security.JwtUtil;
import ntnu.edu.stud.calculator.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Authentication", description = "APIs for user authentication and authorization. Handles user registration, login, and token management.")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account in the system. The username must be unique and the password must meet security requirements. " +
                     "Upon successful registration, a JWT token is returned for immediate authentication."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content = @Content(
                schema = @Schema(implementation = LoginResponse.class),
                examples = @ExampleObject(
                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiJ9...\", \"user\": {\"username\": \"john_doe\"}, \"error\": null}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input - Username already exists or password does not meet requirements",
            content = @Content(
                examples = @ExampleObject(
                    value = "Username already exists"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred during registration"
        )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @Parameter(
            description = "User registration details",
            required = true,
            example = "{\"username\": \"john_doe\", \"password\": \"securePassword123\"}"
        )
        @RequestBody User user) {
        try {
            logger.info("Registering new user: {}", user.getUsername());
            User newUser = userService.registerUser(user.getUsername(), user.getPassword());
            String token = jwUtil.generateToken(newUser.getUsername());
            LoginResponse response = new LoginResponse(token, newUser);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration", e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @Operation(
        summary = "Login user",
        description = "Authenticates a user with their username and password. If successful, returns a JWT token that should be " +
                     "included in subsequent API requests in the Authorization header as 'Bearer <token>'."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                schema = @Schema(implementation = LoginResponse.class),
                examples = @ExampleObject(
                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiJ9...\", \"user\": {\"username\": \"john_doe\"}, \"error\": null}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication failed - Invalid username or password",
            content = @Content(
                examples = @ExampleObject(
                    value = "{\"token\": null, \"user\": null, \"error\": \"Invalid credentials\"}"
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @Parameter(
            description = "User login credentials",
            required = true,
            example = "{\"username\": \"john_doe\", \"password\": \"securePassword123\"}"
        )
        @RequestBody User loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword())
                .map(user -> {
                    String token = jwUtil.generateToken(user.getUsername());
                    LoginResponse response = new LoginResponse(token, user);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(new LoginResponse(null, null)));
    }

    @Operation(
        summary = "Refresh JWT token",
        description = "Generates a new JWT token using an existing valid token. This endpoint should be called when the current token " +
                     "is about to expire. The new token should replace the old one in subsequent requests."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token refreshed successfully",
            content = @Content(
                schema = @Schema(implementation = LoginResponse.class),
                examples = @ExampleObject(
                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiJ9...\", \"user\": null, \"error\": null}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token refresh failed - Invalid or expired token",
            content = @Content(
                examples = @ExampleObject(
                    value = "{\"token\": null, \"user\": null, \"error\": \"Invalid credentials\"}"
                )
            )
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
        @Parameter(
            description = "Bearer token in Authorization header. Must be a valid JWT token.",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiJ9..."
        )
        @RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            String username = jwUtil.validateTokenAndGetUsername(token);
                    
            if (username != null) {
                String newToken = jwUtil.generateToken(username);
                return ResponseEntity.ok(new LoginResponse(newToken, null));
            }
        }
        return ResponseEntity.status(401).body(new LoginResponse(null, null));
    }
}

@Schema(description = "Response object containing authentication token and user information")
class LoginResponse {
    @Schema(description = "JWT token for authentication")
    private String token;
    
    @Schema(description = "User information (null for token refresh)")
    private User user;
    
    @Schema(description = "Error message if authentication failed")
    private String error;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
        this.error = token == null ? "Invalid credentials" : null;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public String getError() {
        return error;
    }
}