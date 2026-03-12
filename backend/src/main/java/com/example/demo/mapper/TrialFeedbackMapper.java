package com.example.demo.mapper;

import com.example.demo.model.TrialFeedback;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TrialFeedbackMapper {
    @Select("SELECT * FROM trial_feedback WHERE id = #{id}")
    TrialFeedback findById(Long id);

    @Select("SELECT * FROM trial_feedback WHERE trial_id = #{trialId}")
    TrialFeedback findByTrialId(Long trialId);

    @Select("SELECT * FROM trial_feedback WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<TrialFeedback> findByUserId(Long userId);

    @Select("SELECT * FROM trial_feedback ORDER BY create_time DESC LIMIT #{limit}")
    List<TrialFeedback> findRecent(Integer limit);

    @Select("SELECT * FROM trial_feedback ORDER BY update_time DESC, create_time DESC")
    List<TrialFeedback> findAll();

    @Insert("INSERT INTO trial_feedback(trial_id, user_id, rating, feedback, issues, purchase_intent, status, provider_reply, create_time, update_time) " +
            "VALUES(#{trialId}, #{userId}, #{rating}, #{feedback}, #{issues}, #{purchaseIntent}, #{status}, #{providerReply}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TrialFeedback feedback);

    @Update("UPDATE trial_feedback SET rating=#{rating}, feedback=#{feedback}, issues=#{issues}, purchase_intent=#{purchaseIntent}, status=#{status}, provider_reply=#{providerReply}, update_time=NOW() WHERE trial_id=#{trialId}")
    int updateByTrialId(TrialFeedback feedback);

    @Update("UPDATE trial_feedback SET status=#{status}, provider_reply=#{providerReply}, update_time=NOW() WHERE id=#{id}")
    int updateStatusAndReply(@Param("id") Long id, @Param("status") String status, @Param("providerReply") String providerReply);

    @Select("SELECT AVG(rating) FROM trial_feedback WHERE trial_id = #{trialId}")
    Double getAverageRating(Long trialId);
}
