package com.company.payment_service.dto;

public class PaymentStatsResponse {

    private long totalPayments;
    private long successfulPayments;
    private long pendingPayments;

    public PaymentStatsResponse(
            long totalPayments,
            long successfulPayments,
            long pendingPayments) {

        this.totalPayments = totalPayments;
        this.successfulPayments = successfulPayments;
        this.pendingPayments = pendingPayments;
    }

    public long getTotalPayments() {
        return totalPayments;
    }

    public long getSuccessfulPayments() {
        return successfulPayments;
    }

    public long getPendingPayments() {
        return pendingPayments;
    }
}