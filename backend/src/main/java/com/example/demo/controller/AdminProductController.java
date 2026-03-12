package com.example.demo.controller;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
public class AdminProductController {

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/pending")
    public Map<String, Object> pending() {
        List<Product> list = productMapper.findPendingPartner();
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        return result;
    }

    @PutMapping("/{id}/approve")
    public Map<String, Object> approve(@PathVariable Long id) {
        productMapper.updateStatus(id, "ACTIVE");
        Product p = productMapper.findById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", p);
        result.put("message", "已上架");
        return result;
    }

    @PutMapping("/{id}/offline")
    public Map<String, Object> offline(@PathVariable Long id) {
        productMapper.updateStatus(id, "OFFLINE");
        Product p = productMapper.findById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", p);
        result.put("message", "已下架");
        return result;
    }
}
