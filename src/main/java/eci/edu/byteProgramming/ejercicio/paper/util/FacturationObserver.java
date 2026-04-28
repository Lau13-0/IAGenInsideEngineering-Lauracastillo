package eci.edu.byteProgramming.ejercicio.paper.util;

public class FacturationObserver implements PaymentObserver {
    private Facturation facturation;
    
    public FacturationObserver(Facturation facturation) {
        this.facturation = facturation;
    }
    
    @Override
    public void onPaymentSuccess(PaymentMethod payment, String customerName, 
                                String customerEmail, String productId) {
        System.out.println("[FACTURATION] Notified of successful payment");
        facturation.generateInvoice(payment, customerName, productId);
    }
    
    @Override
    public void onPaymentFailed(PaymentMethod payment, String customerEmail) {
        System.out.println("[FACTURATION] Payment failed - no invoice generated");
    }
}
