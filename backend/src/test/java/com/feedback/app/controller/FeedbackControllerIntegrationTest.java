package com.feedback.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedback.app.TestConfig;
import com.feedback.app.dto.FeedbackRequest;
import com.feedback.app.model.Feedback;
import com.feedback.app.repository.FeedbackRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class FeedbackControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        feedbackRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        feedbackRepository.deleteAll();
    }

    @Test
    void submitFeedback_WithValidData_ShouldReturnCreated() throws Exception {
        FeedbackRequest request = new FeedbackRequest(
                "Jane Smith",
                "jane@example.com",
                "Excellent experience!"
        );

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Jane Smith")))
                .andExpect(jsonPath("$.email", is("jane@example.com")))
                .andExpect(jsonPath("$.message", is("Excellent experience!")));

        List<Feedback> feedbacks = feedbackRepository.findAll();
        assertEquals(1, feedbacks.size());
        assertEquals("Jane Smith", feedbacks.get(0).getName());
    }

    @Test
    void submitFeedback_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        FeedbackRequest request = new FeedbackRequest("", "invalid-email", "");

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.message", containsString("name: Name is required")))
                .andExpect(jsonPath("$.message", containsString("email: Please provide a valid email")))
                .andExpect(jsonPath("$.message", containsString("message: Message is required")));
    }

    @Test
    void getStats_ShouldReturnStats() throws Exception {
        feedbackRepository.save(Feedback.builder()
                .name("Test User")
                .email("test@example.com")
                .message("Test message")
                .build());

        mockMvc.perform(get("/api/feedback/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFeedbacks", is(1)))
                .andExpect(jsonPath("$.lastUpdated").exists());
    }

    @Test
    void getAllFeedbacks_ShouldReturnAllFeedbacks() throws Exception {
        Feedback feedback1 = feedbackRepository.save(Feedback.builder()
                .name("User 1")
                .email("user1@example.com")
                .message("First feedback")
                .build());

        Feedback feedback2 = feedbackRepository.save(Feedback.builder()
                .name("User 2")
                .email("user2@example.com")
                .message("Second feedback")
                .build());

        mockMvc.perform(get("/api/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", isIn(List.of("User 1", "User 2"))))
                .andExpect(jsonPath("$[1].name", isIn(List.of("User 1", "User 2"))));
    }
}