package lk.orm.project01.bo;

import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.PatientDAO;
import lk.orm.project01.dao.PaymentDAO;
import lk.orm.project01.dto.PaymentDTO;
import lk.orm.project01.entity.Patient;
import lk.orm.project01.entity.Payment;
import lk.orm.project01.exception.PaymentException;
import lk.orm.project01.exception.ValidationException;
import lk.orm.project01.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO =
        (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PAYMENT);
    private final PatientDAO patientDAO =
        (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PATIENT);

    @Override
    public boolean processPayment(PaymentDTO dto)
            throws PaymentException, ValidationException, Exception {

        validatePaymentDTO(dto);


        Patient patient = patientDAO.findById(dto.getPatientId());
        if (patient == null) {
            throw new PaymentException("Patient not found: " + dto.getPatientId());
        }

        Payment payment = new Payment(
            dto.getPaymentId(),
            dto.getAmount(),
            dto.getPaymentDate(),
            dto.getPaymentMethod(),
            dto.getStatus() != null ? dto.getStatus() : "COMPLETED",
            dto.getProgrammeId(),
            dto.getDescription(),
            patient
        );

        return paymentDAO.save(payment);
    }

    @Override
    public boolean updatePayment(PaymentDTO dto)
            throws PaymentException, ValidationException, Exception {

        validatePaymentDTO(dto);

        Payment existing = paymentDAO.findById(dto.getPaymentId());
        if (existing == null) {
            throw new PaymentException("Payment not found: " + dto.getPaymentId());
        }

        existing.setAmount(dto.getAmount());
        existing.setPaymentDate(dto.getPaymentDate());
        existing.setPaymentMethod(dto.getPaymentMethod());
        existing.setStatus(dto.getStatus());
        existing.setProgrammeId(dto.getProgrammeId());
        existing.setDescription(dto.getDescription());

        return paymentDAO.update(existing);
    }

    @Override
    public boolean deletePayment(String paymentId) throws Exception {
        return paymentDAO.delete(paymentId);
    }

    @Override
    public PaymentDTO getPaymentById(String paymentId) throws Exception {
        Payment p = paymentDAO.findById(paymentId);
        return p != null ? convertToDTO(p) : null;
    }

    @Override
    public List<PaymentDTO> getAllPayments() throws Exception {
        return convertToDTOList(paymentDAO.findAll());
    }

    @Override
    public List<PaymentDTO> getPaymentsByPatient(String patientId) throws Exception {
        return convertToDTOList(paymentDAO.findByPatientId(patientId));
    }

    @Override
    public List<PaymentDTO> getPendingPayments() throws Exception {
        return convertToDTOList(paymentDAO.findPendingPayments());
    }

    @Override
    public List<PaymentDTO> getCompletedPayments() throws Exception {
        return convertToDTOList(paymentDAO.findCompletedPayments());
    }

    @Override
    public double getTotalRevenue() throws Exception {
        return paymentDAO.getTotalRevenue();
    }

    @Override
    public String generateNextPaymentId() throws Exception {
        List<Payment> all = paymentDAO.findAll();
        if (all.isEmpty()) return "PAY001";
        int max = 0;
        for (Payment p : all) {
            String id = p.getPaymentId();
            if (id != null && id.startsWith("PAY")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("PAY%03d", max + 1);
    }

    // ---- Validation ----

    private void validatePaymentDTO(PaymentDTO dto) throws ValidationException {
        if (!ValidationUtil.isNotEmpty(dto.getPaymentId())) {
            throw new ValidationException("Payment ID is required.");
        }
        if (!ValidationUtil.isValidAmount(dto.getAmount())) {
            throw new ValidationException("Payment amount must be greater than zero.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getPatientId())) {
            throw new ValidationException("Patient ID is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getPaymentDate())) {
            throw new ValidationException("Payment date is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getPaymentMethod())) {
            throw new ValidationException("Payment method is required.");
        }
    }

    // ---- Conversion Helpers ----

    private PaymentDTO convertToDTO(Payment p) {
        return new PaymentDTO(
            p.getPaymentId(),
            p.getAmount(),
            p.getPaymentDate(),
            p.getPaymentMethod(),
            p.getStatus(),
            p.getProgrammeId(),
            p.getDescription(),
            p.getPatient() != null ? p.getPatient().getPatientId() : "",
            p.getPatient() != null ? p.getPatient().getName() : ""
        );
    }

    private List<PaymentDTO> convertToDTOList(List<Payment> list) {
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payment p : list) dtos.add(convertToDTO(p));
        return dtos;
    }
}
