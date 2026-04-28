package eci.edu.byteProgramming.ejercicio.paper.util;

public class NotificationObserver implements PaymentObserver {
    private Notification notification;
    
    public NotificationObserver(Notification notification) {
        this.notification = notification;
    }
    
    @Override
    public void onPaymentSuccess(PaymentMethod payment, String customerName, 
                                String customerEmail, String productId) {
        System.out.println("[NOTIFICATION] Notified of successful payment");
        notification.sendConfirmationEmail(customerEmail, customerName, payment);
    }
    
    @Override
    public void onPaymentFailed(PaymentMethod payment, String customerEmail) {
        System.out.println("[NOTIFICATION] Notified of failed payment");
        notification.sendFailureNotification(payment, customerEmail);
    }
}
