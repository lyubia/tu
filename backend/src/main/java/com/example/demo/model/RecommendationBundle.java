package com.example.demo.model;

import java.util.List;
import java.util.Map;

public class RecommendationBundle {
    private Solution solution;
    private List<Product> products;
    private Map<String, String> highlights;

    public Solution getSolution() { return solution; }
    public void setSolution(Solution solution) { this.solution = solution; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
    public Map<String, String> getHighlights() { return highlights; }
    public void setHighlights(Map<String, String> highlights) { this.highlights = highlights; }
}

