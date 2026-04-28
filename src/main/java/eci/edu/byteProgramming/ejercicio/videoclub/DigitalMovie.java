package eci.edu.byteProgramming.ejercicio.videoclub;

public class DigitalMovie extends Movie {
    public DigitalMovie(String title, double basePrice, boolean available) {
        super(title, basePrice, available);
    }
    
    @Override
    public String getType() {
        return "Digital";
    }
}
