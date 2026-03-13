package com.example.demo.controller;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.SolutionMapper;
import com.example.demo.model.Product;
import com.example.demo.model.Solution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/solutions")
@CrossOrigin(origins = "*")
public class SolutionController {

    @Autowired
    private SolutionMapper solutionMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 获取方案列表
     */
    @GetMapping
    public Map<String, Object> list(
            @RequestParam(required = false) String industry) {
        
        List<Solution> solutions;
        
        if (industry != null && !industry.isEmpty()) {
            solutions = solutionMapper.findByIndustry(industry);
        } else {
            solutions = solutionMapper.findAll();
        }
        
        // 填充关联产品信息
        for (Solution solution : solutions) {
            if (solution.getProductIds() != null && !solution.getProductIds().isEmpty()) {
                List<Product> products = new ArrayList<>();
                for (String pidStr : solution.getProductIds().split(",")) {
                    try {
                        Long pid = Long.parseLong(pidStr.trim());
                        Product p = productMapper.findById(pid);
                        if (p != null) {
                            products.add(p);
                        }
                    } catch (NumberFormatException ignored) {}
                }
                solution.setProducts(products);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", solutions);
        result.put("total", solutions.size());
        
        return result;
    }

    /**
     * 获取方案详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable Long id) {
        Solution solution = solutionMapper.findById(id);
        
        Map<String, Object> result = new HashMap<>();
        
        if (solution != null) {
            result.put("data", solution);
            
            // 填充关联产品
            if (solution.getProductIds() != null && !solution.getProductIds().isEmpty()) {
                List<Product> products = new ArrayList<>();
                for (String pidStr : solution.getProductIds().split(",")) {
                    try {
                        Long pid = Long.parseLong(pidStr.trim());
                        Product p = productMapper.findById(pid);
                        if (p != null) {
                            products.add(p);
                        }
                    } catch (NumberFormatException ignored) {}
                }
                result.put("products", products);
            }
        } else {
            result.put("error", "方案不存在");
        }
        
        return result;
    }

    /**
     * 获取方案行业分类
     */
    @GetMapping("/industries")
    public Map<String, Object> industries() {
        List<String> industries = Arrays.asList(
                "物流", "电商", "制造", "零售", "园区", "跨境"
        );
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", industries);
        
        return result;
    }
}
