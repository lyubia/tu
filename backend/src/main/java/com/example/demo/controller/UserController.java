package com.example.demo.controller;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        
        String username = params.get("username");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");
        String industry = params.get("industry");
        
        // 检查用户名是否已存在
        User existUser = userMapper.findByUsername(username);
        if (existUser != null) {
            result.put("code", 400);
            result.put("message", "用户名已存在");
            return ResponseEntity.ok(result);
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setIndustry(industry);
        user.setRole("BUYER");
        
        userMapper.insert(user);
        
        result.put("code", 200);
        result.put("message", "注册成功");
        result.put("data", user);
        return ResponseEntity.ok(result);
    }

    /**
     * 更新用户行业/画像
     */
    @PostMapping("/update-industry")
    public ResponseEntity<Map<String, Object>> updateIndustry(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        Long userId = Long.valueOf(params.get("userId").toString());
        String industry = (String) params.get("industry");
        
        userMapper.updateIndustry(userId, industry);
        
        result.put("code", 200);
        result.put("message", "更新成功");
        return ResponseEntity.ok(result);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        User user = userMapper.findById(userId);
        if (user == null) {
            result.put("code", 404);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        
        result.put("code", 200);
        result.put("data", user);
        return ResponseEntity.ok(result);
    }
}
