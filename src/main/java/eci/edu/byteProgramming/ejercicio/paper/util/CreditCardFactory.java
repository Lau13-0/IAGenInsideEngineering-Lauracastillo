package eci.edu.byteProgramming.ejercicio.paper.util;

public class CreditCardFactory implements PaymentFactory {
    
    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        String cardNumber = "4532015112830366";
        String cardHolderName = "John Doe";
        String expirationDate = "12/25";
        String cvv = "123";
        String address = "123 Main St";
        
        return new CreditCardPayment(amount, customerId, description, 
                                     cardNumber, cardHolderName, expirationDate, cvv, address);
    }
}
