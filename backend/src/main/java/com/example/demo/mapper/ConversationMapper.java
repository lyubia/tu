package com.example.demo.mapper;

import com.example.demo.model.Conversation;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ConversationMapper {
    
    @Select("SELECT * FROM conversation WHERE id = #{id}")
    Conversation findById(Long id);

    @Select("SELECT * FROM conversation WHERE session_uuid = #{sessionUuid}")
    Conversation findBySessionUuid(String sessionUuid);

    @Select("SELECT * FROM conversation WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Conversation> findByUserId(Long userId);

    @Insert("INSERT INTO conversation(user_id, session_uuid, status) VALUES(#{userId}, #{sessionUuid}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Conversation conversation);

    @Update("UPDATE conversation SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
