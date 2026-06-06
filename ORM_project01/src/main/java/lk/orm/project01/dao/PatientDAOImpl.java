package lk.orm.project01.dao;

import lk.orm.project01.config.FactoryConfiguration;
import lk.orm.project01.entity.Patient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class PatientDAOImpl implements PatientDAO {

    @Override
    public boolean save(Patient entity) throws Exception {
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
    public boolean update(Patient entity) throws Exception {
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
    public boolean delete(String patientId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Patient patient = session.get(Patient.class, patientId);
            if (patient != null) {
                session.remove(patient);
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
    public Patient findById(String patientId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(Patient.class, patientId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Patient> findAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<Patient> query = session.createQuery("FROM Patient ORDER BY name", Patient.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    // ---- Custom HQL Queries ----

    @Override
    public List<Patient> searchByName(String name) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            // HQL: case-insensitive partial name search
            Query<Patient> query = session.createQuery(
                "FROM Patient p WHERE LOWER(p.name) LIKE :name ORDER BY p.name",
                Patient.class
            );
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public Patient findByNic(String nic) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<Patient> query = session.createQuery(
                "FROM Patient p WHERE p.nic = :nic", Patient.class
            );
            query.setParameter("nic", nic);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }


    @Override
    public List<Patient> findPatientsEnrolledInAllProgrammes() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT p FROM Patient p
                WHERE (
                    SELECT COUNT(DISTINCT pp.therapyProgramme.programmeId)
                    FROM PatientProgramme pp
                    WHERE pp.patient = p
                ) = (
                    SELECT COUNT(tp) FROM TherapyProgramme tp
                )
                """;
            Query<Patient> query = session.createQuery(hql, Patient.class);
            return query.list();
        } finally {
            session.close();
        }
    }


    @Override
    public List<Patient> findPatientsWithProgrammes() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT DISTINCT p FROM Patient p
                LEFT JOIN FETCH p.patientProgrammes pp
                LEFT JOIN FETCH pp.therapyProgramme
                ORDER BY p.name
                """;
            Query<Patient> query = session.createQuery(hql, Patient.class);
            return query.list();
        } finally {
            session.close();
        }
    }
}
