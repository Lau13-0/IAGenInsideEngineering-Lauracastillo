package eci.edu.byteProgramming.ejercicio.paper.util;

public class VirtualStoreDemo {
    public static void main(String[] args) {
        System.out.println("Virtual Store - Payment System Demo");
        System.out.println();
        
        Inventory inventory = new Inventory();
        Facturation facturation = new Facturation();
        Notification notification = new Notification();
        
        ECIPayment eciPayment = new ECIPayment();
        eciPayment.addObserver(new InventoryObserver(inventory));
        eciPayment.addObserver(new FacturationObserver(facturation));
        eciPayment.addObserver(new NotificationObserver(notification));
        
        System.out.println("------- TEST 1: Credit Card Payment -------");
        PaymentFactory creditCardFactory = new CreditCardFactory();
        eciPayment.processPayment(creditCardFactory, 1200.00, "CUST001", 
                                 "Purchase of Gaming Laptop", "Laura Castillo", 
                                 "laura@example.com", "LAPTOP001");
        
        System.out.println("\n\n------- TEST 2: PayPal Payment -------");
        PaymentFactory paypalFactory = new PaypalFactory();
        eciPayment.processPayment(paypalFactory, 800.00, "CUST002", 
                                 "Purchase of Smartphone", "Juan Perez", 
                                 "juan@example.com", "PHONE001");
        
        System.out.println("\n\n------- TEST 3: Cryptocurrency Payment -------");
        PaymentFactory cryptoFactory = new CryptoFactory();
        eciPayment.processPayment(cryptoFactory, 45.99, "CUST003", 
                                 "Purchase of Java Programming Book", "Maria Garcia", 
                                 "maria@example.com", "BOOK001");
    }
}
