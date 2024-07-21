package com.example.creditcard.service;

import com.example.creditcard.entity.Payment;
import com.example.creditcard.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(Payment payment) {
    	payment.setTimestamp(LocalDateTime.now());
        payment.setStatus("PENDING");
        logger.info("Processing payment with amount: {}", payment.getAmount());
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment processed successfully with ID: {}", savedPayment.getId());
        return savedPayment;
    }

    public List<Payment> getAllPayments() {
    	logger.info("Retrieving all payments");
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        logger.info("Retrieving payment with ID: {}", id);
        return paymentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Payment with ID: {} not found", id);
                    return new RuntimeException("Payment not found");
                });
    }

    public Payment updatePaymentStatus(Long id, String status) {
        logger.info("Updating payment with ID: {} to status: {}", id, status);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Payment with ID: {} not found", id);
                    return new RuntimeException("Payment not found");
                });
        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);
        logger.info("Payment status updated successfully with ID: {}", updatedPayment.getId());
        return updatedPayment;
    }
}

