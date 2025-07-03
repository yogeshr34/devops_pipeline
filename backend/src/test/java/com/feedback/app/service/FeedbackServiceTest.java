// backend/src/test/java/com/feedback/app/service/FeedbackServiceTest.java
package com.feedback.app.service;

import com.feedback.app.dto.FeedbackRequest;
import com.feedback.app.dto.StatsResponse;
import com.feedback.app.model.Feedback;
import com.feedback.app.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private Feedback feedback;
    private FeedbackRequest feedbackRequest;

    @BeforeEach
    void setUp() {
        feedback = Feedback.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .message("Great service!")
                .build();

        feedbackRequest = new FeedbackRequest(
                "John Doe",
                "john@example.com",
                "Great service!"
        );
    }

    @Test
    void saveFeedback_ShouldReturnSavedFeedback() {
        // Arrange
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        Feedback savedFeedback = feedbackService.saveFeedback(feedbackRequest);

        // Assert
        assertNotNull(savedFeedback);
        assertEquals("John Doe", savedFeedback.getName());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void getAllFeedbacks_ShouldReturnAllFeedbacks() {
        // Arrange
        List<Feedback> feedbacks = Arrays.asList(feedback);
        when(feedbackRepository.findAll()).thenReturn(feedbacks);

        // Act
        List<Feedback> result = feedbackService.getAllFeedbacks();

        // Assert
        assertEquals(1, result.size());
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    void getStats_ShouldReturnStats() {
        // Arrange
        when(feedbackRepository.count()).thenReturn(5L);

        // Act
        StatsResponse stats = feedbackService.getStats();

        // Assert
        assertEquals(5L, stats.getTotalFeedbacks());
        assertNotNull(stats.getLastUpdated());
        verify(feedbackRepository, times(1)).count();
    }
}