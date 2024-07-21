package com.example.creditcard.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.creditcard.entity.Payment;
import com.example.creditcard.response.ApiResponse;
import com.example.creditcard.response.ErrorResponse;
import com.example.creditcard.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	 private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	 private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> processPayment(@RequestBody Payment payment) {
        logger.info("Received request to process payment");
        Payment processedPayment = paymentService.processPayment(payment);
        ApiResponse<Payment> response = new ApiResponse<>(true, processedPayment, "Payment processed successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<Payment>>> getAllPayments() {
        logger.info("Received request to retrieve all payments");
        Iterable<Payment> payments = paymentService.getAllPayments();
        ApiResponse<Iterable<Payment>> response = new ApiResponse<>(true, payments, "Payments retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        logger.info("Received request to retrieve payment with ID: {}", id);
        try {
            Payment payment = paymentService.getPaymentById(id);
            ApiResponse<Payment> response = new ApiResponse<>(true, payment, "Payment retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse("Payment Not Found", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        logger.info("Received request to update payment status for ID: {} to status: {}", id, status);
        try {
            Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
            ApiResponse<Payment> response = new ApiResponse<>(true, updatedPayment, "Payment status updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse("Payment Not Found", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }
}
