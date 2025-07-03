package com.feedback.app.service;

import com.feedback.app.dto.FeedbackRequest;
import com.feedback.app.dto.StatsResponse;
import com.feedback.app.model.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback saveFeedback(FeedbackRequest feedbackRequest);
    List<Feedback> getAllFeedbacks();
    StatsResponse getStats();
}