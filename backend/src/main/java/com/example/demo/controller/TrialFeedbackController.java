package com.example.demo.controller;

import com.example.demo.model.TrialFeedback;
import com.example.demo.service.TrialFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trials")
public class TrialFeedbackController {

    @Autowired
    private TrialFeedbackService feedbackService;

    /**
     * POST /api/trials/:id/feedback - 提交反馈
     */
    @PostMapping("/{id}/feedback")
    public ResponseEntity<Map<String, Object>> submitFeedback(
            @PathVariable Long id,
            @RequestBody TrialFeedback feedback) {
        try {
            // 设置试用 ID
            feedback.setTrialId(id);
            
            TrialFeedback created = feedbackService.createFeedback(feedback);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", created);
            result.put("message", "反馈提交成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * GET /api/trials/:id/feedback - 获取试用反馈
     */
    @GetMapping("/{id}/feedback")
    public ResponseEntity<Map<String, Object>> getFeedbackByTrialId(@PathVariable Long id) {
        TrialFeedback feedback = feedbackService.getFeedbackByTrialId(id);
        Map<String, Object> result = new HashMap<>();
        if (feedback != null) {
            result.put("success", true);
            result.put("data", feedback);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("error", "反馈不存在");
            return ResponseEntity.status(404).body(result);
        }
    }
}
