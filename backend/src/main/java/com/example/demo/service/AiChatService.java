package com.example.demo.service;

import com.example.demo.model.ChatRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AiChatService {

    @Value("${ai.api.model:MiniMax-M2.5}")
    private String aiApiModel;

    @Value("${ai.api.url:}")
    private String aiApiUrl;

    @Value("${ai.api.key:}")
    private String aiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> chat(ChatRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        // 构建消息列表
        List<Map<String, String>> messages = new ArrayList<>();
        
        // 添加历史消息
        if (request.getHistory() != null) {
            for (ChatRequest.Conversation conv : request.getHistory()) {
                Map<String, String> msg = new HashMap<>();
                msg.put("role", conv.getRole());
                msg.put("content", conv.getContent());
                messages.add(msg);
            }
        }
        
        // 添加当前消息
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", request.getMessage());
        messages.add(userMessage);

        // 如果有配置 AI API，使用外部 API
        if (aiApiUrl != null && !aiApiUrl.isEmpty()) {
            try {
                Map<String, Object> body = new HashMap<>();
                body.put("model", aiApiModel);
                body.put("messages", messages);
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                if (aiApiKey != null && !aiApiKey.isEmpty()) {
                    headers.set("Authorization", "Bearer " + aiApiKey);
                }
                
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
                ResponseEntity<Map> result = restTemplate.postForEntity(aiApiUrl, entity, Map.class);
                
                if (result.getStatusCode() == HttpStatus.OK && result.getBody() != null) {
                    Map<String, Object> resultBody = result.getBody();
                    response.put("success", true);
                    response.put("message", resultBody.get("choices") != null 
                        ? ((List<?>) resultBody.get("choices")).get(0)
                        : resultBody.get("content"));
                    response.put("raw", resultBody);
                }
            } catch (Exception e) {
                response.put("success", false);
                response.put("error", e.getMessage());
            }
        } else {
            // 没有配置 API 时，返回模拟响应
            String reply = generateMockReply(request.getMessage());
            Map<String, Object> choice = new HashMap<>();
            Map<String, String> msg = new HashMap<>();
            msg.put("role", "assistant");
            msg.put("content", reply);
            choice.put("message", msg);
            
            response.put("success", true);
            response.put("message", choice);
        }
        
        return response;
    }

    private String generateMockReply(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "请输入您的问题";
        }
        
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("你好") || lowerMessage.contains("hello") || lowerMessage.contains("hi")) {
            return "您好！我是 AI 助手，很高兴为您服务。请问有什么可以帮助您的？";
        } else if (lowerMessage.contains("试用") || lowerMessage.contains("trial")) {
            return "关于试用服务，我可以帮您创建试用账号、查询试用状态或提交反馈。请告诉我您的具体需求。";
        } else if (lowerMessage.contains("价格") || lowerMessage.contains("price") || lowerMessage.contains("收费")) {
            return "我们的定价根据您的需求定制。请联系我们的销售团队获取详细报价。";
        } else if (lowerMessage.contains("帮助") || lowerMessage.contains("help")) {
            return "我可以帮助您：\n1. 解答产品相关问题\n2. 指导试用流程\n3. 提供技术支持\n4. 收集反馈意见\n\n请告诉我您需要什么帮助？";
        } else {
            return "感谢您的提问！您的问题我已经收到。我们的团队会尽快回复您。如果您有其他问题，请随时告诉我。";
        }
    }
}
