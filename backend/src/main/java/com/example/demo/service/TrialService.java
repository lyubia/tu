package com.example.demo.service;

import com.example.demo.mapper.TrialMapper;
import com.example.demo.model.Trial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrialService {

    @Autowired
    private TrialMapper trialMapper;

    public Trial createTrial(Trial trial) {
        if (trial.getStatus() == null) {
            trial.setStatus("pending");
        }
        trial.setCreateTime(LocalDateTime.now());
        trial.setUpdateTime(LocalDateTime.now());
        trialMapper.insert(trial);
        return trial;
    }

    public Trial getTrialById(Long id) {
        return trialMapper.findById(id);
    }

    public List<Trial> getTrialsByUserId(Long userId) {
        return trialMapper.findByUserId(userId);
    }

    public boolean updateTrialStatus(Long id, String status) {
        return trialMapper.updateStatus(id, status) > 0;
    }

    public boolean updateTrialEnvironment(Long id, String environmentUrl) {
        return trialMapper.updateEnvironment(id, environmentUrl) > 0;
    }
}
