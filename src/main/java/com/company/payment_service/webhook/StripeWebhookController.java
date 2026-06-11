package com.company.payment_service.webhook;

import com.company.payment_service.entity.PaymentEntity;
import com.company.payment_service.repository.PaymentRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private final PaymentRepository paymentRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public StripeWebhookController(
            PaymentRepository paymentRepository) {

        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/stripe")
    public String handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {

            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret);

        } catch (SignatureVerificationException e) {

            return "Invalid Signature";
        }

        if ("payment_intent.succeeded".equals(event.getType())) {

            PaymentIntent paymentIntent =
                    (PaymentIntent) event
                            .getDataObjectDeserializer()
                            .getObject()
                            .orElse(null);

            if (paymentIntent != null) {

                paymentRepository
                        .findByPaymentIntentId(
                                paymentIntent.getId())
                        .ifPresent(payment -> {

                            payment.setStatus("succeeded");

                            paymentRepository.save(payment);
                        });
            }
        }

        if ("payment_intent.payment_failed".equals(event.getType())) {

            PaymentIntent paymentIntent =
                    (PaymentIntent) event
                            .getDataObjectDeserializer()
                            .getObject()
                            .orElse(null);

            if (paymentIntent != null) {

                paymentRepository
                        .findByPaymentIntentId(
                                paymentIntent.getId())
                        .ifPresent(payment -> {

                            payment.setStatus("failed");

                            paymentRepository.save(payment);
                        });
            }
        }

        return "Webhook Processed";
    }
}