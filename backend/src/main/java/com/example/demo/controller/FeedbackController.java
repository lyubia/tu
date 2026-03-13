package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    /**
     * 提交方案/产品反馈
     */
    @PostMapping("/submit")
    public Map<String, Object> submit(@RequestBody Map<String, Object> feedback) {
        Map<String, Object> result = new HashMap<>();
        
        // 模拟保存反馈
        Long feedbackId = new Random().nextLong(1000, 9999);
        
        result.put("success", true);
        result.put("message", "反馈已提交，感谢您的意见！");
        result.put("feedbackId", feedbackId);
        
        return result;
    }

    /**
     * 获取用户反馈历史
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> userFeedback(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> feedbacks = new ArrayList<>();
        
        // 模拟数据
        feedbacks.add(Map.of(
            "id", 1,
            "productName", "智能仓储管理系统",
            "feedbackType", "INTERESTED",
            "feedbackText", "功能很强大",
            "createTime", "2026-03-12"
        ));
        
        result.put("data", feedbacks);
        
        return result;
    }
}
