package eci.edu.byteProgramming.ejercicio.videoclub;

public enum Membership {
    BASIC("Basica", 0.0),
    PREMIUM("Premium", 0.20);
    
    private final String displayName;
    private final double discountPercentage;
    
    Membership(String displayName, double discountPercentage) {
        this.displayName = displayName;
        this.discountPercentage = discountPercentage;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
}
