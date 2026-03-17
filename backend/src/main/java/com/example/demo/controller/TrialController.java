package com.example.demo.controller;

import com.example.demo.model.Trial;
import com.example.demo.service.TrialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trials")
public class TrialController {

    @Autowired
    private TrialService trialService;

    /**
     * POST /api/trials - 创建试用
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTrial(@RequestBody Trial trial) {
        try {
            Trial created = trialService.createTrial(trial);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", created);
            result.put("message", "试用创建成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * GET /api/trials/:id - 试用详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTrialById(@PathVariable Long id) {
        Trial trial = trialService.getTrialById(id);
        Map<String, Object> result = new HashMap<>();
        if (trial != null) {
            result.put("success", true);
            result.put("data", trial);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("error", "试用不存在");
            return ResponseEntity.status(404).body(result);
        }
    }

    /**
     * GET /api/trials/user/:userId - 用户试用列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getTrialsByUserId(@PathVariable Long userId) {
        List<Trial> trials = trialService.getTrialsByUserId(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", trials);
        result.put("count", trials.size());
        return ResponseEntity.ok(result);
    }
}
