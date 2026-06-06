package lk.orm.project01.dao;

import lk.orm.project01.config.FactoryConfiguration;
import lk.orm.project01.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(Payment entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Payment entity) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String paymentId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Payment payment = session.get(Payment.class, paymentId);
            if (payment != null) {
                session.remove(payment);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Payment findById(String paymentId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(Payment.class, paymentId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Payment> findAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            // JOIN FETCH patient to avoid lazy loading issues
            String hql = """
                SELECT p FROM Payment p
                JOIN FETCH p.patient
                ORDER BY p.paymentDate DESC
                """;
            Query<Payment> query = session.createQuery(hql, Payment.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Payment> findByPatientId(String patientId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<Payment> query = session.createQuery(
                "FROM Payment p WHERE p.patient.patientId = :patientId ORDER BY p.paymentDate DESC",
                Payment.class
            );
            query.setParameter("patientId", patientId);
            return query.list();
        } finally {
            session.close();
        }
    }

    /**
     * HQL Requirement #3: Retrieve all pending payments.
     */
    @Override
    public List<Payment> findPendingPayments() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT p FROM Payment p
                JOIN FETCH p.patient
                WHERE p.status = 'PENDING'
                ORDER BY p.paymentDate ASC
                """;
            Query<Payment> query = session.createQuery(hql, Payment.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Payment> findCompletedPayments() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT p FROM Payment p
                JOIN FETCH p.patient
                WHERE p.status = 'COMPLETED'
                ORDER BY p.paymentDate DESC
                """;
            Query<Payment> query = session.createQuery(hql, Payment.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public double getTotalRevenue() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            // HQL aggregate: sum of all completed payment amounts
            Query<Double> query = session.createQuery(
                "SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p WHERE p.status = 'COMPLETED'",
                Double.class
            );
            Double result = query.uniqueResult();
            return result != null ? result : 0.0;
        } finally {
            session.close();
        }
    }
}
