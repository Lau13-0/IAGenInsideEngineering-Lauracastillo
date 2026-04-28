package eci.edu.byteProgramming.ejercicio.paper.util;

public class PaypalFactory implements PaymentFactory {
    private String email;
    private String paypalTransactionId;
    private String authToken;
    
    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        String email = "user@example.com";
        String authToken = "PP_AUTH_TOKEN_123456789";
        
        return new PayPalPayment(amount, customerId, description, email, authToken);
    }
}
