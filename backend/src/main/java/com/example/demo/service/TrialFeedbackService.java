package com.example.demo.service;

import com.example.demo.mapper.TrialFeedbackMapper;
import com.example.demo.model.TrialFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrialFeedbackService {

    @Autowired
    private TrialFeedbackMapper feedbackMapper;

    public TrialFeedback createFeedback(TrialFeedback feedback) {
        if (feedback.getStatus() == null) {
            feedback.setStatus("pending");
        }
        feedback.setCreateTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        feedbackMapper.insert(feedback);
        return feedback;
    }

    public TrialFeedback getFeedbackById(Long id) {
        return feedbackMapper.findById(id);
    }

    public TrialFeedback getFeedbackByTrialId(Long trialId) {
        return feedbackMapper.findByTrialId(trialId);
    }

    public List<TrialFeedback> getFeedbacksByUserId(Long userId) {
        return feedbackMapper.findByUserId(userId);
    }

    public List<TrialFeedback> getRecentFeedbacks(Integer limit) {
        return feedbackMapper.findRecent(limit != null ? limit : 10);
    }

    public Double getAverageRating(Long trialId) {
        return feedbackMapper.getAverageRating(trialId);
    }

    public boolean updateFeedback(Long trialId, TrialFeedback feedback) {
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.updateByTrialId(feedback) > 0;
    }

    public boolean updateStatusAndReply(Long id, String status, String providerReply) {
        return feedbackMapper.updateStatusAndReply(id, status, providerReply) > 0;
    }
}
