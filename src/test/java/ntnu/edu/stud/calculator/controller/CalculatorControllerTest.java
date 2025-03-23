package ntnu.edu.stud.calculator.controller;

import ntnu.edu.stud.calculator.service.CalculatorService;
import ntnu.edu.stud.calculator.service.CalculationService;
import ntnu.edu.stud.calculator.service.UserService;
import ntnu.edu.stud.calculator.model.Calculation;
import ntnu.edu.stud.calculator.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalculatorService calculatorService;

    @Mock
    private CalculationService calculationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calculatorController).build();
    }

    @Test
    void testCalculate() throws Exception {
        // Mock authenticated user
        User mockUser = new User("testuser", "password");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);

        // Mock service behavior
        when(calculatorService.performCalculation("2+2")).thenReturn("4");
        when(calculationService.saveCalculation(eq("2+2"), eq(4.0), any(User.class)))
                .thenReturn(new Calculation("2+2", 4.0, mockUser));

        // Perform request
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"expression\": \"2+2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(4.0));
    }
}
