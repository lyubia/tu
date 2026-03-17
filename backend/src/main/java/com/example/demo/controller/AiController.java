package com.example.demo.controller;

import com.example.demo.model.ChatRequest;
import com.example.demo.service.AiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiChatService aiChatService;

    /**
     * POST /api/ai/chat - AI 对话
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody ChatRequest request) {
        try {
            Map<String, Object> response = aiChatService.chat(request);
            
            Map<String, Object> result = new HashMap<>();
            if (Boolean.TRUE.equals(response.get("success"))) {
                result.put("success", true);
                result.put("data", response.get("message"));
                result.put("raw", response.get("raw"));
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("error", response.get("error"));
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}
