package com.company.payment_service.service;

import com.company.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class StripeServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private StripeService stripeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void stripeServiceShouldLoad() {

        assertNotNull(stripeService);
    }

    @Test
    void paymentRepositoryShouldBeMocked() {

        assertNotNull(paymentRepository);
    }
}