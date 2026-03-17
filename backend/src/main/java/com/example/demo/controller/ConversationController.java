package com.example.demo.controller;

import com.example.demo.model.Conversation;
import com.example.demo.model.Message;
import com.example.demo.model.Requirement;
import com.example.demo.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    /**
     * POST /api/conversation/create - 创建新对话，返回session_uuid
     */
    @PostMapping("/conversation/create")
    public ResponseEntity<Map<String, Object>> createConversation(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Conversation conversation = conversationService.createConversation(userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("session_uuid", conversation.getSessionUuid());
            result.put("conversation_id", conversation.getId());
            result.put("message", "对话创建成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * POST /api/message/save - 保存消息（包含requirements字段）
     */
    @PostMapping("/message/save")
    public ResponseEntity<Map<String, Object>> saveMessage(@RequestBody Map<String, Object> request) {
        try {
            Long conversationId = Long.valueOf(request.get("conversationId").toString());
            String role = request.get("role").toString();
            String content = request.get("content").toString();
            String requirements = request.get("requirements") != null ? request.get("requirements").toString() : null;
            
            Message message = conversationService.saveMessage(conversationId, role, content, requirements);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message_id", message.getId());
            result.put("message", "消息保存成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * GET /api/conversation/history/:userId - 获取用户所有对话列表
     */
    @GetMapping("/conversation/history/{userId}")
    public ResponseEntity<Map<String, Object>> getConversationHistory(@PathVariable Long userId) {
        try {
            List<Conversation> conversations = conversationService.getConversationsByUserId(userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", conversations);
            result.put("count", conversations.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * GET /api/message/:conversationId - 获取某个对话的所有消息
     */
    @GetMapping("/message/{conversationId}")
    public ResponseEntity<Map<String, Object>> getMessages(@PathVariable Long conversationId) {
        try {
            List<Message> messages = conversationService.getMessagesByConversationId(conversationId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", messages);
            result.put("count", messages.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * POST /api/requirement/save - 保存结构化需求
     */
    @PostMapping("/requirement/save")
    public ResponseEntity<Map<String, Object>> saveRequirement(@RequestBody Requirement requirement) {
        try {
            Requirement saved = conversationService.saveRequirement(requirement);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("requirement_id", saved.getId());
            result.put("message", "需求保存成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * GET /api/requirement/:conversationId - 获取对话的需求列表
     */
    @GetMapping("/requirement/{conversationId}")
    public ResponseEntity<Map<String, Object>> getRequirements(@PathVariable Long conversationId) {
        try {
            List<Requirement> requirements = conversationService.getRequirementsByConversationId(conversationId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", requirements);
            result.put("count", requirements.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}
