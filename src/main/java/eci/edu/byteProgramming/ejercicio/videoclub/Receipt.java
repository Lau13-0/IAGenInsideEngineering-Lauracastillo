package eci.edu.byteProgramming.ejercicio.videoclub;

import java.util.List;

public class Receipt {
    private Membership membership;
    private List<Movie> rentedMovies;
    private double subtotal;
    private double discount;
    private double total;
    
    public Receipt(Membership membership, List<Movie> rentedMovies) {
        this.membership = membership;
        this.rentedMovies = rentedMovies;
        calculateTotals();
    }
    
    private void calculateTotals() {
        this.subtotal = rentedMovies.stream()
            .mapToDouble(Movie::getBasePrice)
            .sum();
        
        this.discount = subtotal * membership.getDiscountPercentage();
        this.total = subtotal - discount;
    }
    
    public void print() {
        System.out.println("--- RECIBO DE ALQUILER ---");
        System.out.println("Cliente: " + membership.getDisplayName());
        System.out.println("Peliculas:");
        
        for (Movie movie : rentedMovies) {
            System.out.printf(" - %s (%s) - $%.0f%n", 
                movie.getTitle(), movie.getType(), movie.getBasePrice());
        }
        
        System.out.printf("Subtotal: $%.0f%n", subtotal);
        
        if (discount > 0) {
            System.out.printf("Descuento (%.0f%%): $%.0f%n", 
                membership.getDiscountPercentage() * 100, discount);
        }
        
        System.out.printf("Total a pagar: $%.0f%n", total);
        System.out.println("--------------------------");
        System.out.println("¡Disfrute su pelicula!");
    }
}
