package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.ArrayList;
import java.util.List;

public class ECIPayment {
    private List<PaymentObserver> observers;
    
    public ECIPayment() {
        this.observers = new ArrayList<>();
    }
    
    public void addObserver(PaymentObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(PaymentObserver observer) {
        observers.remove(observer);
    }
    
    public boolean processPayment(PaymentFactory factory, double amount, String customerId, 
                                String description, String customerName, String customerEmail, 
                                String productId) {
        
        System.out.println("========================================");
        System.out.println("ECI Payments: Starting payment process...");
        System.out.println("Customer: " + customerName + " (" + customerEmail + ")");
        System.out.println("Amount: $" + amount);
        System.out.println("Description: " + description);
        System.out.println("========================================");
        
        PaymentMethod payment = factory.createPaymentMethod(amount, customerId, description);
        
        boolean success = payment.processPayment();
        
        System.out.println();
        if (success) {
            System.out.println("Payment processed successfully!");
            System.out.println("Notifying observers...");
            System.out.println();
            notifyPaymentSuccess(payment, customerName, customerEmail, productId);
        } else {
            System.out.println("Payment failed!");
            notifyPaymentFailed(payment, customerEmail);
        }
        
        return success;
    }
    
    private void notifyPaymentSuccess(PaymentMethod payment, String customerName, 
                                    String customerEmail, String productId) {
        for (PaymentObserver observer : observers) {
            observer.onPaymentSuccess(payment, customerName, customerEmail, productId);
            System.out.println();
        }
    }
    
    private void notifyPaymentFailed(PaymentMethod payment, String customerEmail) {
        for (PaymentObserver observer : observers) {
            observer.onPaymentFailed(payment, customerEmail);
        }
    }
}
