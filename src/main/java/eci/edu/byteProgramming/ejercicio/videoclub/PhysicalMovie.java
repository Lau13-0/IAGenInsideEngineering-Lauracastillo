package eci.edu.byteProgramming.ejercicio.videoclub;

public class PhysicalMovie extends Movie {
    public PhysicalMovie(String title, double basePrice, boolean available) {
        super(title, basePrice, available);
    }
    
    @Override
    public String getType() {
        return "Fisica";
    }
}
