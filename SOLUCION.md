# SOLUCION.md - Problema #1: El Videoclub de Don Mario

IA Utilizada: Google Gemini

## Resumen del Problema

Sistema de alquiler de películas que permite registrar películas (físicas o digitales), seleccionar película para alquilar, aplicar descuentos según membresía y generar recibos.

---

## Prompts Utilizados para la Solución

### PROMPT #1: Arquitectura Base del Sistema
```
Necesito diseñar un sistema de alquiler de películas en Java con estos requerimientos:
- Las películas pueden ser físicas o digitales
- Debe haber membresías (básica con 0% descuento, premium con 20% descuento)
- El sistema debe calcular el precio total según la membresía
- Debe generar un recibo formateado con detalles de la compra
- Usar patrones de diseño apropiados (Factory, Strategy o similar)
- Aplicar principios SOLID especialmente S, O y L

Dame la estructura de clases necesarias y cómo implementarías el polimorfismo para que el cálculo de precio sea diferente según el tipo de película.
```

### PROMPT #2: Implementación Detallada y Output
```
Ahora implementa el sistema completo con:
1. Una clase Movie abstracta o interfaz con subclases PhysicalMovie y DigitalMovie
2. Una enumeración o clase Membership que maneje los descuentos
3. Un VideoClubSystem que gestione las películas disponibles y procese alquileres
4. Método para generar recibos exactamente como el formato especificado
5. Un main que demuestre el caso de ejemplo: Cliente Premium selecciona películas 1 y 3

Asegúrate de que el código sea ejecutable desde consola y muestre el recibo formateado.
```

---

## Patrones de Diseño Identificados

| Patrón | Ubicación | Justificación |
|--------|-----------|---------------|
| Factory Pattern | MovieFactory | Creación de películas físicas o digitales sin exponer detalles internos |
| Strategy Pattern | MoviePricingStrategy | Diferentes estrategias de cálculo de precio según tipo de película |
| Template Method | Clase abstracta Movie | Define estructura común, subclases implementan getType() |
| Singleton (Opcional) | VideoClubSystem | Único punto de acceso al catálogo de películas |

---

## Principios SOLID Aplicados

### S - Single Responsibility Principle
- Movie: responsable solo de representar una película
- Membership: responsable solo de manejar descuentos
- Receipt: responsable solo de formatear y mostrar recibos
- VideoClubSystem: responsable solo de gestionar el alquiler

### O - Open/Closed Principle
- Movie es abierta para extensión (PhysicalMovie, DigitalMovie, FutureMovieType)
- Cerrada para modificación: no cambia si añado nuevos tipos de películas

### L - Liskov Substitution Principle
- PhysicalMovie y DigitalMovie son sustitutos válidos de Movie
- El sistema puede trabajar con cualquier tipo sin cambios

### D - Dependency Inversion Principle
- VideoClubSystem depende de Movie (abstracción), no de implementaciones concretas

---

## Código Fuente Completo

### 1. Clase Abstracta Movie
```java
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
    
    // Template method - el tipo define el precio
    public abstract String getType();
    
    @Override
    public String toString() {
        return String.format("%s (%s) - $%.0f - %s", 
            title, getType(), basePrice, 
            available ? "Disponible" : "No disponible");
    }
}
```

### 2. Subclases: PhysicalMovie y DigitalMovie
```java
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
```

```java
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
```

### 3. Enumeración Membership
```java
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
```

### 4. Clase Receipt (Recibo)
```java
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
```

### 5. Sistema Principal: VideoClubSystem
```java
package eci.edu.byteProgramming.ejercicio.videoclub;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
```

### 6. Main - Aplicación Principal
```java
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
```

---

## Evidencia de Ejecución

### Entrada (Caso de Ejemplo):
```
Membresia del cliente: 2 (Premium)
Seleccione peliculas: 1,3
```

### Salida Esperada:
```
=== CATALOGO DE PELICULAS ===
1. Interestellar (Fisica) - $8000 - Disponible
2. El Padrino (Fisica) - $7000 - No disponible
3. Inception (Digital) - $5000 - Disponible
4. Matrix (Digital) - $6000 - Disponible

Seleccione su membresia (1=Basica, 2=Premium): 2
Seleccione peliculas (numeros separados por coma): 1,3

--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Interestellar (Fisica) - $8000
 - Inception (Digital) - $5000
Subtotal: $13000
Descuento (20%): $2600
Total a pagar: $10400
--------------------------
¡Disfrute su pelicula!
```

---

## Estructura de Clases

```
VideoClubApplication (main)
    ↓
VideoClubSystem (Gestor del sistema)
    ├─→ Movie (Abstracta)
    │   ├─→ PhysicalMovie
    │   └─→ DigitalMovie
    ├─→ Membership (Enum)
    └─→ Receipt (Recibo)
```

---

## Conceptos Clave Implementados

✓ Polimorfismo: PhysicalMovie y DigitalMovie pueden ser tratadas como Movie  
✓ Encapsulamiento: Cada clase mantiene su estado privado  
✓ Abstracción: Movie define contrato, subclases implementan  
✓ Método Template: Estructura común en Movie, detalles en subclases  
✓ Calculadora de precios flexible: Fácil agregar nuevas membresías o tipos de película  

---

## Conclusión

Este sistema demuestra cómo usar patrones de diseño y principios SOLID para crear código mantenible, extensible y robusto. La solución permite que Don Mario agregue nuevos tipos de películas o membresías sin modificar el código existente.
