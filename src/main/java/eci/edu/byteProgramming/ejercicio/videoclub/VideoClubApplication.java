package eci.edu.byteProgramming.ejercicio.videoclub;

import java.util.Scanner;

public class VideoClubApplication {
    public static void main(String[] args) {
        VideoClubSystem system = new VideoClubSystem();
        Scanner scanner = new Scanner(System.in);
        
        // Mostrar catálogo
        system.displayCatalog();
        
        // Solicitar membresía
        System.out.print("Seleccione su membresia (1=Basica, 2=Premium): ");
        int membershipChoice = scanner.nextInt();
        Membership membership = (membershipChoice == 2) ? 
            Membership.PREMIUM : Membership.BASIC;
        
        // Solicitar películas
        System.out.print("Seleccione peliculas (numeros separados por coma): ");
        scanner.nextLine(); // consumir salto de línea
        String input = scanner.nextLine();
        
        String[] parts = input.split(",");
        int[] movieIndices = new int[parts.length];
        
        for (int i = 0; i < parts.length; i++) {
            movieIndices[i] = Integer.parseInt(parts[i].trim()) - 1;
        }
        
        // Procesar alquiler y mostrar recibo
        Receipt receipt = system.processRental(membership, movieIndices);
        System.out.println();
        receipt.print();
        
        scanner.close();
    }
}
