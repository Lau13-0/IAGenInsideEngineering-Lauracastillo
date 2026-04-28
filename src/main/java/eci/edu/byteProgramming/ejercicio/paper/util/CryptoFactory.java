package eci.edu.byteProgramming.ejercicio.paper.util;

public class CryptoFactory implements PaymentFactory {
    private String walletAddress;
    private String cryptoType;
    private String token;
    private double walletBalance;
    private String blockchainHash;
    
    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        String walletAddress = "0x742d35Cc6634C0532925a3b844Bc9e7595f";
        String cryptoType = "ETHEREUM";
        double walletBalance = 5000.0;
        
        return new CryptoPayment(amount, customerId, description, 
                               walletAddress, cryptoType, walletBalance);
    }
}
