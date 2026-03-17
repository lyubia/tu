package com.example.demo.controller;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 获取产品列表（支持行业筛选）
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "20") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        
        List<Product> products;
        if (industry != null && !industry.isEmpty()) {
            products = productMapper.findByIndustry(industry, limit);
        } else {
            products = productMapper.findAll();
            if (products.size() > limit) {
                products = products.subList(0, limit);
            }
        }
        
        result.put("code", 200);
        result.put("data", products);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取热门产品
     */
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> popular(@RequestParam(defaultValue = "8") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        
        List<Product> products = productMapper.findPopular(limit);
        
        result.put("code", 200);
        result.put("data", products);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取产品详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        Product product = productMapper.findById(id);
        if (product == null) {
            result.put("code", 404);
            result.put("message", "产品不存在");
            return ResponseEntity.ok(result);
        }
        
        result.put("code", 200);
        result.put("data", product);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有行业分类
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> categories() {
        Map<String, Object> result = new HashMap<>();
        
        List<String> industries = productMapper.findAllIndustries();
        
        result.put("code", 200);
        result.put("data", industries);
        return ResponseEntity.ok(result);
    }
}
