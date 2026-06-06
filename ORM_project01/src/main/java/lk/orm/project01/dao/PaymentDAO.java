package lk.orm.project01.dao;

import lk.orm.project01.entity.Payment;
import java.util.List;


public interface PaymentDAO extends CrudDAO<Payment, String> {


    List<Payment> findByPatientId(String patientId) throws Exception;


    List<Payment> findPendingPayments() throws Exception;


    List<Payment> findCompletedPayments() throws Exception;


    double getTotalRevenue() throws Exception;
}
