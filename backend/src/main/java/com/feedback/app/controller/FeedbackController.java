package com.feedback.app.controller;

import com.feedback.app.dto.FeedbackRequest;
import com.feedback.app.dto.StatsResponse;
import com.feedback.app.model.Feedback;
import com.feedback.app.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;

    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .build();
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @GetMapping("/stats")
    public StatsResponse getStats() {
        long count = feedbackRepository.count();
        LocalDateTime lastUpdated = feedbackRepository.findTopByOrderByCreatedAtDesc()
                .map(Feedback::getCreatedAt)
                .orElse(LocalDateTime.now());
        
        return StatsResponse.builder()
                .totalFeedbacks(count)
                .lastUpdated(lastUpdated)
                .build();
    }
}