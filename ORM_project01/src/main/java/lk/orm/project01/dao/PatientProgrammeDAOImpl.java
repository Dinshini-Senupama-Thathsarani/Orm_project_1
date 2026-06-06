package lk.orm.project01.dao;

import lk.orm.project01.config.FactoryConfiguration;
import lk.orm.project01.entity.PatientProgramme;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class PatientProgrammeDAOImpl implements PatientProgrammeDAO {

    @Override
    public boolean save(PatientProgramme entity) throws Exception {
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
    public boolean update(PatientProgramme entity) throws Exception {
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
    public boolean delete(Long id) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            PatientProgramme pp = session.get(PatientProgramme.class, id);
            if (pp != null) {
                session.remove(pp);
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
    public PatientProgramme findById(Long id) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(PatientProgramme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<PatientProgramme> findAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<PatientProgramme> query = session.createQuery(
                "FROM PatientProgramme", PatientProgramme.class
            );
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<PatientProgramme> findByPatientId(String patientId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            // JOIN FETCH to load programme details eagerly
            String hql = """
                SELECT pp FROM PatientProgramme pp
                JOIN FETCH pp.therapyProgramme
                WHERE pp.patient.patientId = :patientId
                """;
            Query<PatientProgramme> query = session.createQuery(hql, PatientProgramme.class);
            query.setParameter("patientId", patientId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<PatientProgramme> findByProgrammeId(String programmeId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT pp FROM PatientProgramme pp
                JOIN FETCH pp.patient
                WHERE pp.therapyProgramme.programmeId = :programmeId
                """;
            Query<PatientProgramme> query = session.createQuery(hql, PatientProgramme.class);
            query.setParameter("programmeId", programmeId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean isEnrolled(String patientId, String programmeId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT COUNT(pp) FROM PatientProgramme pp
                WHERE pp.patient.patientId = :patientId
                AND pp.therapyProgramme.programmeId = :programmeId
                """;
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("patientId", patientId);
            query.setParameter("programmeId", programmeId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }
}
