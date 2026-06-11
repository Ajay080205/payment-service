package com.company.payment_service.repository;

import com.company.payment_service.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository
        extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByPaymentIntentId(
            String paymentIntentId);

    List<PaymentEntity> findByStatus(
            String status);

    long countByStatus(
            String status);

    List<PaymentEntity> findByPaymentIntentIdContaining(
            String paymentIntentId);
}