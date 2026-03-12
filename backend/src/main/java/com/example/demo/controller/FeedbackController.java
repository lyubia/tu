package com.example.demo.controller;

import com.example.demo.mapper.TrialFeedbackMapper;
import com.example.demo.model.TrialFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private TrialFeedbackMapper feedbackMapper;

    @GetMapping("/all")
    public Map<String, Object> all() {
        List<TrialFeedback> list = feedbackMapper.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        String status = (String) request.getOrDefault("status", "SUBMITTED");
        String providerReply = (String) request.getOrDefault("providerReply", null);

        feedbackMapper.updateStatusAndReply(id, status, providerReply);

        TrialFeedback updated = feedbackMapper.findById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", updated);
        result.put("message", "更新成功");
        return result;
    }
}

