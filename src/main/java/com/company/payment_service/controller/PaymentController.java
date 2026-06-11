package com.company.payment_service.controller;

import com.company.payment_service.dto.PaymentRequest;
import com.company.payment_service.dto.PaymentStatsResponse;
import com.company.payment_service.dto.RefundRequest;
import com.company.payment_service.entity.PaymentEntity;
import com.company.payment_service.repository.PaymentRepository;
import com.company.payment_service.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final StripeService stripeService;
    private final PaymentRepository paymentRepository;

    public PaymentController(
            StripeService stripeService,
            PaymentRepository paymentRepository) {

        this.stripeService = stripeService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/create")
    public Map<String, String> createPayment(
            @Valid @RequestBody PaymentRequest request)
            throws StripeException {

        PaymentIntent paymentIntent =
                stripeService.createPayment(request);

        Map<String, String> response = new HashMap<>();

        response.put("paymentIntentId",
                paymentIntent.getId());

        response.put("clientSecret",
                paymentIntent.getClientSecret());

        return response;
    }

    @GetMapping("/{id}")
    public PaymentEntity getPayment(
            @PathVariable Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment Not Found"));
    }

    @GetMapping
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("/status/{status}")
    public List<PaymentEntity> getPaymentsByStatus(
            @PathVariable String status) {

        return paymentRepository.findByStatus(status);
    }

    @PostMapping("/refund")
    public Map<String, String> refundPayment(
            @RequestBody RefundRequest request)
            throws StripeException {

        Refund refund =
                stripeService.createRefund(
                        request.getPaymentIntentId());

        Map<String, String> response =
                new HashMap<>();

        response.put("refundId",
                refund.getId());

        response.put("status",
                refund.getStatus());

        return response;
    }

    @GetMapping("/stats")
    public PaymentStatsResponse getStats() {

        long total =
                paymentRepository.count();

        long successful =
                paymentRepository.countByStatus(
                        "succeeded");

        long pending =
                paymentRepository.countByStatus(
                        "requires_payment_method");

        return new PaymentStatsResponse(
                total,
                successful,
                pending);
    }
    @GetMapping("/paged")
public Page<PaymentEntity> getPaymentsPaged(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    return paymentRepository.findAll(
            PageRequest.of(page, size));
}
       @GetMapping("/search/{paymentIntentId}")
public List<PaymentEntity> searchPayments(
        @PathVariable String paymentIntentId) {

    return paymentRepository
            .findByPaymentIntentIdContaining(
                    paymentIntentId);
}
}