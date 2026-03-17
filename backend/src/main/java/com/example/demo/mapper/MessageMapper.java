package com.example.demo.mapper;

import com.example.demo.model.Message;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MessageMapper {
    
    @Select("SELECT * FROM message WHERE id = #{id}")
    Message findById(Long id);

    @Select("SELECT * FROM message WHERE conversation_id = #{conversationId} ORDER BY created_at ASC")
    List<Message> findByConversationId(Long conversationId);

    @Insert("INSERT INTO message(conversation_id, role, content, requirements) VALUES(#{conversationId}, #{role}, #{content}, #{requirements})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Message message);
}
