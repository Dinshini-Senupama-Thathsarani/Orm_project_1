package lk.orm.project01.bo;

import lk.orm.project01.dto.PaymentDTO;
import lk.orm.project01.exception.PaymentException;
import lk.orm.project01.exception.ValidationException;

import java.util.List;

public interface PaymentBO extends SuperBO {

    boolean processPayment(PaymentDTO dto)
            throws PaymentException, ValidationException, Exception;

    boolean updatePayment(PaymentDTO dto)
            throws PaymentException, ValidationException, Exception;

    boolean deletePayment(String paymentId) throws Exception;

    PaymentDTO getPaymentById(String paymentId) throws Exception;

    List<PaymentDTO> getAllPayments() throws Exception;

    List<PaymentDTO> getPaymentsByPatient(String patientId) throws Exception;

    List<PaymentDTO> getPendingPayments() throws Exception;

    List<PaymentDTO> getCompletedPayments() throws Exception;

    double getTotalRevenue() throws Exception;

    /** Generates the next payment ID (e.g., PAY001, PAY002 ...) */
    String generateNextPaymentId() throws Exception;
}
