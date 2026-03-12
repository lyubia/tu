package com.example.demo.model;

import java.util.List;
import java.util.Map;

public class ChatResponse {
    private String reply;
    private List<Product> recommendedProducts;
    private List<Solution> recommendedSolutions;
    private List<RecommendationBundle> bundles;
    private Map<String, String> requirements;
    private Boolean needsMoreInfo;
    private String nextQuestion;

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    public List<Product> getRecommendedProducts() { return recommendedProducts; }
    public void setRecommendedProducts(List<Product> recommendedProducts) { this.recommendedProducts = recommendedProducts; }
    public List<Solution> getRecommendedSolutions() { return recommendedSolutions; }
    public void setRecommendedSolutions(List<Solution> recommendedSolutions) { this.recommendedSolutions = recommendedSolutions; }
    public List<RecommendationBundle> getBundles() { return bundles; }
    public void setBundles(List<RecommendationBundle> bundles) { this.bundles = bundles; }
    public Map<String, String> getRequirements() { return requirements; }
    public void setRequirements(Map<String, String> requirements) { this.requirements = requirements; }
    public Boolean getNeedsMoreInfo() { return needsMoreInfo; }
    public void setNeedsMoreInfo(Boolean needsMoreInfo) { this.needsMoreInfo = needsMoreInfo; }
    public String getNextQuestion() { return nextQuestion; }
    public void setNextQuestion(String nextQuestion) { this.nextQuestion = nextQuestion; }
}
