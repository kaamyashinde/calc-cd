package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.model.Calculation;
import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.service.CalculationService;
import ntnu.edu.stud.calculator.service.CalculatorService;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(
    name = "Calculator",
    description = "APIs for performing mathematical calculations and managing calculation history. " +
                 "All endpoints require authentication via JWT token in the Authorization header."
)
@SecurityRequirement(name = "bearerAuth")
public class CalculatorController {
    @Autowired
    private CalculatorService calculatorService;
    @Autowired
    private CalculationService calculationService;
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @Operation(
        summary = "Perform calculation",
        description = "Evaluates a mathematical expression and saves the result to the user's calculation history. " +
                     "The expression should be a valid mathematical formula (e.g., '2 + 2', 'sin(45)', 'sqrt(16)'). " +
                     "The result is stored with the user's ID for future reference."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Calculation performed successfully",
            content = @Content(
                schema = @Schema(implementation = Calculation.class),
                examples = @ExampleObject(
                    value = "{\"id\": 1, \"expression\": \"2 + 2\", \"result\": 4.0, \"timestamp\": \"2024-03-20T10:30:00\", \"user\": {\"username\": \"john_doe\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or missing JWT token",
            content = @Content(
                examples = @ExampleObject(
                    value = "Unauthorized"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error or invalid mathematical expression"
        )
    })
    @PostMapping("/calculate")
    public ResponseEntity<Calculation> calculate(
        @Parameter(
            description = "Calculation request containing the mathematical expression to evaluate",
            required = true,
            example = "{\"expression\": \"2 + 2\"}"
        )
        @RequestBody Map<String, String> request) {
        try {
            // Get the authenticated user from the security context
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user == null) {
                logger.error("Unauthorized request");
                return ResponseEntity.status(401).build();
            }

            String expression = request.get("expression");
            logger.info("{} requested calculation: {}", user.getUsername(), expression);
            String result = calculatorService.performCalculation(expression);
            Calculation cal = calculationService.saveCalculation(expression, Double.parseDouble(result), user);
            return ResponseEntity.ok(cal);
        } catch (Exception e) {
            logger.error("Calculation error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /*
    public CalculationResponse calculate(@RequestBody CalculationRequest request) {
        logger.info("Received calculation request: {}", request.getExpression());
        String result = calculatorService.performCalculation(request.getExpression());
        CalculationResponse response = new CalculationResponse(result, request.getExpression());
        return response;
    } */

    //Endpoing for getting all calculations for an user
    @Operation(
        summary = "Get calculation history",
        description = "Retrieves a paginated list of calculations performed by the authenticated user. " +
                     "Results are sorted by timestamp in descending order (newest first). " +
                     "Each page contains metadata about the total number of calculations and pages."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "History retrieved successfully",
            content = @Content(
                schema = @Schema(
                    type = "object",
                    description = "Paginated response containing calculation history and metadata"
                ),
                examples = @ExampleObject(
                    value = "{\"content\": [{\"id\": 1, \"expression\": \"2 + 2\", \"result\": 4.0, \"timestamp\": \"2024-03-20T10:30:00\"}], " +
                            "\"currentPage\": 0, \"totalItems\": 1, \"totalPages\": 1}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Invalid or missing JWT token",
            content = @Content(
                examples = @ExampleObject(
                    value = "Unauthorized"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while fetching history"
        )
    })
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(
        @Parameter(
            description = "Page number (0-based). Default is 0.",
            required = false,
            example = "0"
        )
        @RequestParam(defaultValue = "0") int page,
        @Parameter(
            description = "Number of items per page. Default is 10.",
            required = false,
            example = "10"
        )
        @RequestParam(defaultValue = "10") int size) {
        try {
            // Get the authenticated user from the security context
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user == null) {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            logger.info("Fetching calculation history for user: {}", user.getUsername());
            
            // Get paginated calculations
            Page<Calculation> history = calculationService.getCalculationsForUser(user, page, size);
            
            // Create a response map with metadata
            Map<String, Object> response = new HashMap<>();
            response.put("content", history.getContent());
            response.put("currentPage", history.getNumber());
            response.put("totalItems", history.getTotalElements());
            response.put("totalPages", history.getTotalPages());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching history: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error fetching calculation history");
        }
    }
}
