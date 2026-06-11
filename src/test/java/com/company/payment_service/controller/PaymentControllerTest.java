package com.company.payment_service.controller;

import com.company.payment_service.repository.PaymentRepository;
import com.company.payment_service.service.StripeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentControllerTest {

    @Mock
    private StripeService stripeService;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentController paymentController;

    public PaymentControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void controllerShouldLoad() {

        assertNotNull(paymentController);
    }
}