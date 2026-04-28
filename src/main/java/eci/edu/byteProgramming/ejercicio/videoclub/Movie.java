package eci.edu.byteProgramming.ejercicio.videoclub;

public abstract class Movie {
    private String title;
    private double basePrice;
    private boolean available;
    
    public Movie(String title, double basePrice, boolean available) {
        this.title = title;
        this.basePrice = basePrice;
        this.available = available;
    }
    
    public String getTitle() {
        return title;
    }
    
    public double getBasePrice() {
        return basePrice;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public abstract String getType();
    
    @Override
    public String toString() {
        return String.format("%s (%s) - $%.0f - %s", 
            title, getType(), basePrice, 
            available ? "Disponible" : "No disponible");
    }
}
