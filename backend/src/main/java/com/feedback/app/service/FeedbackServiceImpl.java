package com.feedback.app.service;

import com.feedback.app.dto.FeedbackRequest;
import com.feedback.app.dto.StatsResponse;
import com.feedback.app.model.Feedback;
import com.feedback.app.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional
    public Feedback saveFeedback(FeedbackRequest feedbackRequest) {
        Feedback feedback = Feedback.builder()
                .name(feedbackRequest.getName())
                .email(feedbackRequest.getEmail())
                .message(feedbackRequest.getMessage())
                .build();
        
        return feedbackRepository.save(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public StatsResponse getStats() {
        return StatsResponse.builder()
                .totalFeedbacks(feedbackRepository.count())
                .lastUpdated(LocalDateTime.now())
                .build();
    }
}