# SOLUCION.md - Problema #2: Tienda Virtual

IA Utilizada: Google Gemini

## Resumen del Problema

Sistema de pagos para una tienda virtual que soporta múltiples métodos de pago (tarjeta de crédito, PayPal, criptomonedas) con validación automática, notificación de eventos a múltiples observadores (inventario, facturación, notificaciones) usando patrones de diseño.

---

## Prompts Utilizados para la Solución

### PROMPT #1: 

```
Revisa este código de un sistema de pagos con múltiples métodos (tarjeta, PayPal, cripto).
El código está incompleto y tiene errores. Necesito que:

1. Identifiques qué dos patrones de diseño debería usar aquí
2. Expliques qué interfaz falta para implementar correctamente uno de los patrones
3. Señales todos los errores que encuentres (inicializaciones incorrectas, interfaces incompletas)
4. Digas qué principios SOLID se están violando
5. Me muestres la estructura de clases que debería tener

Las clases actuales son: PaymentMethod (abstracta), CreditCardFactory, PaypalFactory, 
CryptoFactory, ECIPayment, PaymentObserver, Inventory, Facturation, Notification.
```

### PROMPT #2: 

```
Ahora implementa la solución correcta con:

1. Interfaz PaymentFactory que sea un Abstract Factory real
2. Tres implementaciones concretas de PaymentFactory (CreditCardFactory, PayPalFactory, CryptoFactory)
3. Las clases PaymentMethod, CreditCardPayment, PayPalPayment, CryptoPayment que representen el pago
4. Los observadores: InventoryObserver, FacturationObserver, NotificationObserver que implementen PaymentObserver
5. Clase ECIPayment que maneje el flujo completo
6. Una clase de prueba que demuestre el flujo: crear pago, validar, procesar, notificar a tres observadores

Debe funcionar sin emojis, compilar sin errores y mostrar la salida de las notificaciones.
```

---

## Análisis de Patrones de Diseño

### Patrón 1: Abstract Factory

Ubicación: Interface `PaymentFactory` con implementaciones `CreditCardFactory`, `PayPalFactory`, `CryptoFactory`

Justificación: Se necesita crear familias de objetos relacionados (método de pago + validador) sin exponer la lógica de creación. El Abstract Factory permite que el cliente no conozca las clases concretas que crea.

Ventajas:
- Facilita agregar nuevos métodos de pago sin modificar el código existente
- Encapsula la creación de objetos
- Garantiza que se crean las familias correctas de objetos relacionados

### Patrón 2: Observer (Observador)

Ubicación: Interface `PaymentObserver` con implementaciones `InventoryObserver`, `FacturationObserver`, `NotificationObserver`

Justificación: Cuando un pago se procesa exitosamente, múltiples componentes necesitan reaccionar (inventario descontar stock, facturación generar factura, notificaciones enviar email). El patrón Observer desacopla estos componentes.

Ventajas:
- Permite agregar nuevos observadores sin modificar ECIPayment
- Desacoplamiento entre componentes
- Mantiene coherencia: todos se notifican del mismo evento

---

## Problemas Identificados en el Código Original

### Error 1: Constructor en PaymentMethod
```
public PaymentMethod(double amount, String transactionID, String description) {
    this.amount = amount;
    this.customerID = customerID;  // ERROR: customerID nunca se recibió como parámetro
}
```
Solución: Cambiar la firma a incluir customerID como parámetro.

### Error 2: CreditCardFactory no es una Factory, es un PaymentMethod
El nombre sugiere que es una factory, pero extiende PaymentMethod. Causa confusión.
Solución: Crear una interfaz PaymentFactory y hacer que CreditCardFactory la implemente.

### Error 3: No hay observadores implementados
Las clases Inventory, Facturation, Notification no implementan PaymentObserver.
Solución: Hacer que implementen la interfaz y sus métodos.

### Error 4: CustomerID sin inicializar
En el constructor del PaymentMethod, customerID se asigna pero el parámetro no existe.
Solución: Agregar customerID como parámetro en el constructor.

---

## Principios SOLID Aplicados

### S - Single Responsibility Principle
- CreditCardPayment: Responsable solo de validar y procesar tarjeta de crédito
- PayPalPayment: Responsable solo de validar y procesar PayPal
- InventoryObserver: Responsable solo de descontar del inventario
- FacturationObserver: Responsable solo de generar facturas
- NotificationObserver: Responsable solo de enviar notificaciones

### O - Open/Closed Principle
- Abierto para extensión: Nuevos métodos de pago sin modificar código existente
- Cerrado para modificación: ECIPayment no cambia al agregar nuevos observadores

### L - Liskov Substitution Principle
- PaymentFactory: CreditCardFactory, PayPalFactory, CryptoFactory son sustitutos válidos
- PaymentMethod: Todas sus subclases cumplen el contrato
- PaymentObserver: Todos los observadores se pueden usar indistintamente

### I - Interface Segregation Principle
- PaymentObserver define métodos específicos
- ValidatePayment define validación
- No se fuerza a las clases a implementar métodos innecesarios

### D - Dependency Inversion Principle
- ECIPayment depende de PaymentFactory (abstracción), no de clases concretas
- ECIPayment depende de PaymentObserver, no de implementaciones específicas

---

## Estructura de Clases - Diagrama

```
PaymentFactory (interface)
    ├─ CreditCardFactory
    ├─ PayPalFactory
    └─ CryptoFactory

PaymentMethod (abstract)
    ├─ CreditCardPayment
    ├─ PayPalPayment
    └─ CryptoPayment

PaymentObserver (interface)
    ├─ InventoryObserver
    ├─ FacturationObserver
    └─ NotificationObserver

ECIPayment (Gestor)
    - Usa PaymentFactory para crear pagos
    - Gestiona lista de PaymentObserver
    - Coordina el flujo de pago
```

---

## Código Fuente Completo

### 1. Interfaz PaymentFactory

```java
package eci.edu.byteProgramming.ejercicio.paper.util;

public interface PaymentFactory {
    PaymentMethod createPaymentMethod(double amount, String customerId, String description);
}
```

### 2. Interfaz PaymentMethod (actualizada)

```java
package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.Date;

public abstract class PaymentMethod {
    protected double amount;
    protected String transactionID;
    protected String customerID;
    protected String currency;
    protected Date timestamp;
    protected PaymentStatus status;
    protected String description;

    public PaymentMethod(double amount, String customerId, String description) {
        this.amount = amount;
        this.customerID = customerId;  // CORREGIDO
        this.description = description;
        this.currency = "USD";
        this.status = PaymentStatus.PENDING;
        this.timestamp = new Date();
        this.transactionID = generateTransactionId();
    }

    public abstract boolean processPayment();
    public abstract boolean validatePaymentMethod();
    public abstract String getPaymentMethod();

    protected String generateTransactionId() {
        long timestamp = System.currentTimeMillis();
        int random = (int)(Math.random() * 9999);
        return String.format("TXN%d%04d", timestamp, random);
    }

    public double getAmount() { return amount; }
    public String getTransactionId() { return transactionID; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public String getCustomerId() { return customerID; }
    public String getDescription() { return description; }
    public Date getTimestamp() { return timestamp; }
}
```

### 3. CreditCardFactory (implementa PaymentFactory)

```java
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
```

### 4. CreditCardPayment (implementa PaymentMethod)

```java
package eci.edu.byteProgramming.ejercicio.paper.util;

public class CreditCardPayment extends PaymentMethod {
    private String number;
    private String name;
    private String expirationDate;
    private String cvv;
    private String cardType;
    private String address;
    
    public CreditCardPayment(double amount, String customerId, String description,
                            String number, String name, String expirationDate, 
                            String cvv, String address) {
        super(amount, customerId, description);
        this.number = number;
        this.name = name;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.cardType = determineCardType(number);
        this.address = address;
    }

    @Override
    public boolean validatePaymentMethod() {
        return validateCardNumber() && validateCVV() && validateExpirationDate();
    }
    
    private boolean validateCardNumber() {
        return number != null && number.length() >= 13 && number.length() <= 19;
    }
    
    private boolean validateCVV() {
        return cvv != null && cvv.length() >= 3 && cvv.length() <= 4;
    }
    
    private boolean validateExpirationDate() {
        return expirationDate != null && expirationDate.matches("\\d{2}/\\d{2}");
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("Processing Credit Card payment...");
        
        if (!validatePaymentMethod()) {
            System.out.println("Credit Card validation failed!");
            setStatus(PaymentStatus.FAILED);
            return false;
        }
        
        setStatus(PaymentStatus.PROCESSING);
        
        try {
            Thread.sleep(1500);
            System.out.println("Contacting bank for card: " + maskCardNumber());
            System.out.println("Payment authorized by bank");
            
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "CREDIT_CARD";
    }
    
    private String determineCardType(String cardNumber) {
        if (cardNumber.startsWith("4")) return "VISA";
        if (cardNumber.startsWith("5")) return "MASTERCARD";
        if (cardNumber.startsWith("3")) return "AMEX";
        return "UNKNOWN";
    }
    
    public String maskCardNumber() {
        return "**** **** **** " + number.substring(number.length() - 4);
    }
    
    public String getCardHolderName() { return name; }
    public String getCardType() { return cardType; }
}
```

### 5. PayPalFactory (implementa PaymentFactory)

```java
package eci.edu.byteProgramming.ejercicio.paper.util;

public class PayPalFactory implements PaymentFactory {
    
    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        String email = "user@example.com";
        String authToken = "PP_AUTH_TOKEN_123456789";
        
        return new PayPalPayment(amount, customerId, description, email, authToken);
    }
}
```

### 6. PayPalPayment (implementa PaymentMethod)

```java
package eci.edu.byteProgramming.ejercicio.paper.util;

public class PayPalPayment extends PaymentMethod {
    private String email;
    private String paypalTransactionId;
    private String authToken;
    
    public PayPalPayment(double amount, String customerId, String description,
                        String email, String authToken) {
        super(amount, customerId, description);
        this.email = email;
        this.authToken = authToken;
    }
    
    @Override
    public boolean validatePaymentMethod() {
        return validateEmail() && validateAuthToken();
    }
    
    private boolean validateEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    private boolean validateAuthToken() {
        return authToken != null && authToken.length() > 10;
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("Processing PayPal payment...");
        
        if (!validatePaymentMethod()) {
            System.out.println("PayPal validation failed!");
            setStatus(PaymentStatus.FAILED);
            return false;
        }
        
        setStatus(PaymentStatus.PROCESSING);
        
        try {
            Thread.sleep(1500);
            this.paypalTransactionId = "PP" + System.currentTimeMillis();
            System.out.println("PayPal payment authorized for: " + email);
            
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "PAYPAL";
    }
    
    public String getEmail() { return email; }
    public String getPaypalTransactionId() { return paypalTransactionId; }
}
```

### 7. CryptoFactory (implementa PaymentFactory)

```java
package eci.edu.byteProgramming.ejercicio.paper.util;

public class CryptoFactory implements PaymentFactory {
    
    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        String walletAddress = "0x742d35Cc6634C0532925a3b844Bc9e7595f";
        String cryptoType = "ETHEREUM";
        double walletBalance = 5000.0;
        
        return new CryptoPayment(amount, customerId, description, 
                               walletAddress, cryptoType, walletBalance);
    }
}
```

### 8. CryptoPayment (implementa PaymentMethod)

```java
package eci.edu.byteProgramming.ejercicio.paper.util;

public class CryptoPayment extends PaymentMethod {
    private String walletAddress;
    private String cryptoType;
    private double walletBalance;
    private String blockchainHash;
    
    public CryptoPayment(double amount, String customerId, String description,
                        String walletAddress, String cryptoType, double walletBalance) {
        super(amount, customerId, description);
        this.walletAddress = walletAddress;
        this.cryptoType = cryptoType;
        this.walletBalance = walletBalance;
    }
    
    @Override
    public boolean validatePaymentMethod() {
        return validateWalletAddress() && validateBalance();
    }
    
    private boolean validateWalletAddress() {
        return walletAddress != null && walletAddress.length() >= 26;
    }
    
    private boolean validateBalance() {
        return walletBalance >= amount;
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("Processing Cryptocurrency payment...");
        
        if (!validatePaymentMethod()) {
            System.out.println("Crypto validation failed!");
            setStatus(PaymentStatus.FAILED);
            return false;
        }
        
        setStatus(PaymentStatus.PROCESSING);
        
        try {
            Thread.sleep(2500);
            this.blockchainHash = generateBlockchainHash();
            System.out.println("Transaction broadcasted to blockchain");
            System.out.println("Blockchain hash: " + blockchainHash);
            
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "CRYPTOCURRENCY";
    }
    
    private String generateBlockchainHash() {
        return "0x" + Integer.toHexString((int)(Math.random() * Integer.MAX_VALUE));
    }
    
    public String getWalletAddress() { return walletAddress; }
    public String getCryptoType() { return cryptoType; }
    public String getBlockchainHash() { return blockchainHash; }
}
```

### 9. InventoryObserver (implementa PaymentObserver)

```java
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
```

### 10. FacturationObserver (implementa PaymentObserver)

```java
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
```

### 11. NotificationObserver (implementa PaymentObserver)

```java
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
```

### 12. ECIPayment (actualizado)

```java
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
```

### 13. Clase de Prueba: VirtualStoreDemo

```java
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
        PaymentFactory paypalFactory = new PayPalFactory();
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
```

---

## Conceptos Clave Implementados

Polimorfismo: CreditCardPayment, PayPalPayment, CryptoPayment son tratados como PaymentMethod
Encapsulamiento: Cada clase mantiene su estado privado y métodos protegidos
Abstracción: PaymentMethod define contrato, subclases implementan detalles
Abstract Factory: PaymentFactory permite crear familias de objetos sin exponer clases concretas
Observer: Múltiples observadores reaccionan a eventos de pago sin acoplamiento
Inyección de dependencias: Los observadores reciben sus dependencias en el constructor
Separación de responsabilidades: Cada clase tiene un único propósito claro

---

## Evidencia de Ejecución Esperada

```
Virtual Store - Payment System Demo

------- TEST 1: Credit Card Payment -------
========================================
ECI Payments: Starting payment process...
Customer: Laura Castillo (laura@example.com)
Amount: $1200.0
Description: Purchase of Gaming Laptop
========================================
Processing Credit Card payment...
Contacting bank for card: **** **** **** 0366
Payment authorized by bank

Payment processed successfully!
Notifying observers...

[INVENTORY] Notified of successful payment
Inventory: Discounted 1 units of Gaming Laptop
   Remaining stock: 4

[FACTURATION] Notified of successful payment
Facturation: Invoice generated
   Invoice Number: INV-1001
   Company: ECI Payments Corp (NIT 900123456-1)
   Customer: Laura Castillo (ID: CUST001)
   Product: LAPTOP001
   Subtotal: $1200.00 COP
   Tax (19%): $228.00 COP
   Total: $1428.00 COP
   Transaction ID: TXN1234567890000
   Date: [timestamp]
   Payment Method: CREDIT_CARD
   Address: Calle 100 #45-30, Bogotá D.C., Bogotá D.C., Colombia
   ----------------------------------------

[NOTIFICATION] Notified of successful payment
Notification: Sending confirmation email
   To: laura@example.com
   From: noreply@eciPayments.com
   Subject: Payment Confirmation - TXN1234567890000
   Dear Laura Castillo,
   Your payment of $1200.0 has been processed successfully via CREDIT_CARD
   Transaction ID: TXN1234567890000
   Thank you for your purchase!


------- TEST 2: PayPal Payment -------
[similar output for PayPal]

------- TEST 3: Cryptocurrency Payment -------
[similar output for Crypto]
```


## Respuestas a Objetivos de Aprendizaje

### 1. ¿Qué dos patrones de diseño se están utilizando?

**Patrón 1: Abstract Factory**
- Interfaz PaymentFactory con implementaciones CreditCardFactory, PayPalFactory, CryptoFactory
- Crea familias de objetos relacionados (factory + payment method)

**Patrón 2: Observer**
- Interfaz PaymentObserver con implementaciones InventoryObserver, FacturationObserver, NotificationObserver
- Notifica automáticamente a observadores cuando ocurren eventos de pago

### 2. ¿Qué clases/interfaces hacen falta?

Hacen falta:
- Interfaz `PaymentFactory` (completamente ausente)
- Clases concretas `CreditCardPayment`, `PayPalPayment`, `CryptoPayment` (se confundían con factories)
- Clases observadores: `InventoryObserver`, `FacturationObserver`, `NotificationObserver`

### 3. ¿El diagrama proporciona información suficiente?

El diagrama UML proporciona información, pero hay puntos de mejora:
- Aclarar que CreditCardFactory debe ser una factory pattern, no una clase de pago
- Mostrar explícitamente la interfaz PaymentFactory
- Mostrar la relación between ECIPayment y los observadores

### 4. ¿Qué errores del código identificaste?

Errores encontrados:
- Error 1: `this.customerID = customerID;` cuando customerID no es parámetro
- Error 2: Nombres confusos (CreditCardFactory extiende PaymentMethod)
- Error 3: Falta la interfaz PaymentFactory completamente
- Error 4: No hay implementaciones de PaymentObserver
- Error 5: En Inventory y otros, falta implementar la interfaz PaymentObserver

### 5. ¿Cómo corriges el código?

Se corrigieron:
- Agregando PaymentFactory como interfaz
- Creando clases de pago (CreditCardPayment, etc.)
- Haciendo que factories implementen PaymentFactory
- Implementando PaymentObserver en clases de observadores
- Corrigiendo el constructor de PaymentMethod
- Removiendo confusión entre factories y métodos de pago

### 6. Pruebas ejecutadas

El código compila sin errores y se puede ejecutar mostrando:
- Tres pagos exitosos (tarjeta, PayPal, cripto)
- Notificación a los tres observadores
- Validación correcta de cada método
- Transacciones con IDs únicos

---

## Conclusión

Este sistema demuestra cómo usar Abstract Factory y Observer para crear un sistema de pagos flexible, extensible y mantenible. Los patrones de diseño permiten agregar nuevos métodos de pago y nuevos observadores sin modificar el código existente, cumpliendo perfectamente con los principios SOLID y creando una arquitectura robusta.
