package com.company.payment_service.service;

import com.company.payment_service.dto.PaymentRequest;
import com.company.payment_service.entity.PaymentEntity;
import com.company.payment_service.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private static final Logger logger =
            LoggerFactory.getLogger(StripeService.class);

    private final PaymentRepository paymentRepository;

    public StripeService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentIntent createPayment(PaymentRequest request)
            throws StripeException {

        logger.info(
                "Creating payment for amount: {} currency: {}",
                request.getAmount(),
                request.getCurrency());

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(request.getAmount())
                        .setCurrency(request.getCurrency())
                        .build();

        PaymentIntent paymentIntent =
                PaymentIntent.create(params);

        PaymentEntity payment = new PaymentEntity();

        payment.setPaymentIntentId(paymentIntent.getId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus(paymentIntent.getStatus());

        paymentRepository.save(payment);

        logger.info(
                "Payment created successfully. PaymentIntentId: {}",
                paymentIntent.getId());

        return paymentIntent;
    }

    public Refund createRefund(String paymentIntentId)
            throws StripeException {

        logger.info(
                "Refund initiated for PaymentIntentId: {}",
                paymentIntentId);

        RefundCreateParams params =
                RefundCreateParams.builder()
                        .setPaymentIntent(paymentIntentId)
                        .build();

        Refund refund = Refund.create(params);

        logger.info(
                "Refund successful. RefundId: {}",
                refund.getId());

        return refund;
    }
}