package com.example.demo.controller;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partner/products")
@CrossOrigin(origins = "*")
public class PartnerProductController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public Map<String, Object> myProducts(@RequestParam Long ownerUserId) {
        List<Product> list = productMapper.findByOwnerUserId(ownerUserId);
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        return result;
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody Map<String, Object> request) {
        Long ownerUserId = Long.valueOf(request.get("ownerUserId").toString());
        User owner = userMapper.findById(ownerUserId);
        if (owner == null || owner.getRole() == null || !"PROVIDER".equalsIgnoreCase(owner.getRole())) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", "提交方用户不存在或不是供应方");
            return result;
        }

        Product p = new Product();
        p.setName((String) request.getOrDefault("name", ""));
        p.setCategory((String) request.getOrDefault("category", ""));
        p.setDescription((String) request.getOrDefault("description", ""));
        p.setCapability((String) request.getOrDefault("capability", ""));
        p.setScenarios((String) request.getOrDefault("scenarios", ""));
        p.setVersion((String) request.getOrDefault("version", ""));
        p.setProviderName((String) request.getOrDefault("providerName", owner.getName()));
        p.setSourceType("PARTNER");
        p.setSourceName((String) request.getOrDefault("sourceName", p.getProviderName()));
        p.setSourceUrl((String) request.getOrDefault("sourceUrl", null));
        p.setExternalDemoUrl((String) request.getOrDefault("externalDemoUrl", null));
        p.setCustomers((String) request.getOrDefault("customers", null));
        p.setCases((String) request.getOrDefault("cases", null));
        p.setOwnerUserId(ownerUserId);
        p.setPopularity(0);
        p.setStatus("DRAFT");

        if (request.get("price") != null && !request.get("price").toString().isEmpty()) {
            p.setPrice(new BigDecimal(request.get("price").toString()));
        }

        productMapper.insert(p);

        Map<String, Object> result = new HashMap<>();
        result.put("data", p);
        result.put("message", "提交成功，等待审核");
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Long ownerUserId = Long.valueOf(request.get("ownerUserId").toString());
        Product existing = productMapper.findById(id);
        if (existing == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", "产品不存在");
            return result;
        }
        if (existing.getOwnerUserId() == null || !existing.getOwnerUserId().equals(ownerUserId)) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", "无权限编辑该产品");
            return result;
        }

        existing.setName((String) request.getOrDefault("name", existing.getName()));
        existing.setCategory((String) request.getOrDefault("category", existing.getCategory()));
        existing.setDescription((String) request.getOrDefault("description", existing.getDescription()));
        existing.setCapability((String) request.getOrDefault("capability", existing.getCapability()));
        existing.setScenarios((String) request.getOrDefault("scenarios", existing.getScenarios()));
        existing.setVersion((String) request.getOrDefault("version", existing.getVersion()));
        existing.setProviderName((String) request.getOrDefault("providerName", existing.getProviderName()));
        existing.setSourceName((String) request.getOrDefault("sourceName", existing.getSourceName()));
        existing.setSourceUrl((String) request.getOrDefault("sourceUrl", existing.getSourceUrl()));
        existing.setExternalDemoUrl((String) request.getOrDefault("externalDemoUrl", existing.getExternalDemoUrl()));
        existing.setCustomers((String) request.getOrDefault("customers", existing.getCustomers()));
        existing.setCases((String) request.getOrDefault("cases", existing.getCases()));

        if (request.get("price") != null && !request.get("price").toString().isEmpty()) {
            existing.setPrice(new BigDecimal(request.get("price").toString()));
        }

        productMapper.updateEditable(existing);
        Product updated = productMapper.findById(id);

        Map<String, Object> result = new HashMap<>();
        result.put("data", updated);
        result.put("message", "更新成功");
        return result;
    }
}
