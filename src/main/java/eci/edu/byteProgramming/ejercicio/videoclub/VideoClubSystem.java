package eci.edu.byteProgramming.ejercicio.videoclub;

import java.util.ArrayList;
import java.util.List;

public class VideoClubSystem {
    private List<Movie> catalog;
    
    public VideoClubSystem() {
        this.catalog = new ArrayList<>();
        initializeCatalog();
    }
    
    private void initializeCatalog() {
        catalog.add(new PhysicalMovie("Interestellar", 8000, true));
        catalog.add(new PhysicalMovie("El Padrino", 7000, false));
        catalog.add(new DigitalMovie("Inception", 5000, true));
        catalog.add(new DigitalMovie("Matrix", 6000, true));
    }
    
    public void displayCatalog() {
        System.out.println("\n=== CATALOGO DE PELICULAS ===");
        for (int i = 0; i < catalog.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, catalog.get(i));
        }
        System.out.println();
    }
    
    public Movie getMovieByIndex(int index) {
        if (index < 0 || index >= catalog.size()) {
            return null;
        }
        return catalog.get(index);
    }
    
    public int getCatalogSize() {
        return catalog.size();
    }
    
    public Receipt processRental(Membership membership, int[] movieIndices) {
        List<Movie> rentedMovies = new ArrayList<>();
        
        for (int index : movieIndices) {
            Movie movie = getMovieByIndex(index);
            if (movie != null && movie.isAvailable()) {
                rentedMovies.add(movie);
            }
        }
        
        return new Receipt(membership, rentedMovies);
    }
}
