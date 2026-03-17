package com.example.demo.mapper;

import com.example.demo.model.Requirement;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RequirementMapper {
    
    @Select("SELECT * FROM requirement WHERE id = #{id}")
    Requirement findById(Long id);

    @Select("SELECT * FROM requirement WHERE conversation_id = #{conversationId}")
    List<Requirement> findByConversationId(Long conversationId);

    @Insert("INSERT INTO requirement(user_id, conversation_id, name, scene, vehicle_count, transport_type, " +
            "pain_point, budget, warehouse_type, sku_count, tags, status) " +
            "VALUES(#{userId}, #{conversationId}, #{name}, #{scene}, #{vehicleCount}, #{transportType}, " +
            "#{painPoint}, #{budget}, #{warehouseType}, #{skuCount}, #{tags}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Requirement requirement);

    @Update("UPDATE requirement SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
