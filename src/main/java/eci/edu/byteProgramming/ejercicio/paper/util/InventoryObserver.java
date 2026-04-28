package eci.edu.byteProgramming.ejercicio.paper.util;

public class InventoryObserver implements PaymentObserver {
    private Inventory inventory;
    
    public InventoryObserver(Inventory inventory) {
        this.inventory = inventory;
    }
    
    @Override
    public void onPaymentSuccess(PaymentMethod payment, String customerName, 
                                String customerEmail, String productId) {
        System.out.println("[INVENTORY] Notified of successful payment");
        inventory.discountProduct(productId, 1);
    }
    
    @Override
    public void onPaymentFailed(PaymentMethod payment, String customerEmail) {
        System.out.println("[INVENTORY] Payment failed - no inventory changes needed");
    }
}
