package org.example.grc.DTO;

public class ChatBotResponse {
    private String refinedDescription;
    private double clarityScore;
    private String estimatedDelay;

    private String nextStep;

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getRefinedDescription() { return refinedDescription; }
    public void setRefinedDescription(String refinedDescription) { this.refinedDescription = refinedDescription; }

    public double getClarityScore() { return clarityScore; }
    public void setClarityScore(double clarityScore) { this.clarityScore = clarityScore; }

    public String getEstimatedDelay() { return estimatedDelay; }
    public void setEstimatedDelay(String estimatedDelay) { this.estimatedDelay = estimatedDelay; }
}