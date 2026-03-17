package com.example.demo.service;

import com.example.demo.mapper.ConversationMapper;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.mapper.RequirementMapper;
import com.example.demo.model.Conversation;
import com.example.demo.model.Message;
import com.example.demo.model.Requirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RequirementMapper requirementMapper;

    /**
     * 创建新对话
     */
    public Conversation createConversation(Long userId) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setSessionUuid(UUID.randomUUID().toString());
        conversation.setStatus(1); // 1=进行中
        conversationMapper.insert(conversation);
        return conversation;
    }

    /**
     * 根据sessionUuid获取对话
     */
    public Conversation getConversationBySessionUuid(String sessionUuid) {
        return conversationMapper.findBySessionUuid(sessionUuid);
    }

    /**
     * 获取用户的对话历史
     */
    public List<Conversation> getConversationsByUserId(Long userId) {
        return conversationMapper.findByUserId(userId);
    }

    /**
     * 结束对话
     */
    public boolean finishConversation(Long id) {
        return conversationMapper.updateStatus(id, 2) > 0;
    }

    /**
     * 保存消息
     */
    public Message saveMessage(Long conversationId, String role, String content, String requirements) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setRole(role);
        message.setContent(content);
        message.setRequirements(requirements);
        messageMapper.insert(message);
        
        // 如果有结构化需求，同时保存
        if (requirements != null && !requirements.isEmpty()) {
            // 这里可以解析requirements并保存到requirement表
            // 简化处理，先不自动创建requirement记录
        }
        
        return message;
    }

    /**
     * 获取对话的所有消息
     */
    public List<Message> getMessagesByConversationId(Long conversationId) {
        return messageMapper.findByConversationId(conversationId);
    }

    /**
     * 保存结构化需求
     */
    public Requirement saveRequirement(Requirement requirement) {
        requirementMapper.insert(requirement);
        return requirement;
    }

    /**
     * 获取对话的结构化需求
     */
    public List<Requirement> getRequirementsByConversationId(Long conversationId) {
        return requirementMapper.findByConversationId(conversationId);
    }
}
