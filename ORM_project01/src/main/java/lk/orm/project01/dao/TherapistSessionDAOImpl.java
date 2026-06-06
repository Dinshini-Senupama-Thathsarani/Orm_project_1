package lk.orm.project01.dao;

import lk.orm.project01.config.FactoryConfiguration;
import lk.orm.project01.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class TherapistSessionDAOImpl implements TherapistSessionDAO {

    @Override
    public boolean save(TherapySession entity) throws Exception {
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
    public boolean update(TherapySession entity) throws Exception {
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
    public boolean delete(String sessionId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            TherapySession ts = session.get(TherapySession.class, sessionId);
            if (ts != null) {
                session.remove(ts);
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
    public TherapySession findById(String sessionId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(TherapySession.class, sessionId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapySession> findAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            // JOIN FETCH to load patient and therapist eagerly
            String hql = """
                SELECT DISTINCT ts FROM TherapySession ts
                JOIN FETCH ts.patient
                JOIN FETCH ts.therapist
                JOIN FETCH ts.therapyProgramme
                ORDER BY ts.sessionDate DESC
                """;
            Query<TherapySession> query = session.createQuery(hql, TherapySession.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapySession> findByTherapistId(String therapistId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                FROM TherapySession ts
                WHERE ts.therapist.therapistId = :therapistId
                ORDER BY ts.sessionDate DESC
                """;
            Query<TherapySession> query = session.createQuery(hql, TherapySession.class);
            query.setParameter("therapistId", therapistId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapySession> findByPatientId(String patientId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                FROM TherapySession ts
                WHERE ts.patient.patientId = :patientId
                ORDER BY ts.sessionDate DESC
                """;
            Query<TherapySession> query = session.createQuery(hql, TherapySession.class);
            query.setParameter("patientId", patientId);
            return query.list();
        } finally {
            session.close();
        }
    }

    /**
     * Scheduling conflict detection:
     * Checks if a therapist already has a session at the same date and time.
     * Excludes the current session ID when updating (to allow same-slot updates).
     */
    @Override
    public boolean hasConflict(String therapistId, String sessionDate,
                               String sessionTime, String excludeSessionId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql;
            Query<Long> query;

            if (excludeSessionId != null) {
                // Exclude the current session when checking for update conflicts
                hql = """
                    SELECT COUNT(ts) FROM TherapySession ts
                    WHERE ts.therapist.therapistId = :therapistId
                    AND ts.sessionDate = :sessionDate
                    AND ts.sessionTime = :sessionTime
                    AND ts.sessionId <> :excludeId
                    AND ts.status <> 'CANCELLED'
                    """;
                query = session.createQuery(hql, Long.class);
                query.setParameter("therapistId", therapistId);
                query.setParameter("sessionDate", sessionDate);
                query.setParameter("sessionTime", sessionTime);
                query.setParameter("excludeId", excludeSessionId);
            } else {
                hql = """
                    SELECT COUNT(ts) FROM TherapySession ts
                    WHERE ts.therapist.therapistId = :therapistId
                    AND ts.sessionDate = :sessionDate
                    AND ts.sessionTime = :sessionTime
                    AND ts.status <> 'CANCELLED'
                    """;
                query = session.createQuery(hql, Long.class);
                query.setParameter("therapistId", therapistId);
                query.setParameter("sessionDate", sessionDate);
                query.setParameter("sessionTime", sessionTime);
            }

            Long count = query.uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapySession> findByStatus(String status) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<TherapySession> query = session.createQuery(
                "FROM TherapySession ts WHERE ts.status = :status ORDER BY ts.sessionDate",
                TherapySession.class
            );
            query.setParameter("status", status);
            return query.list();
        } finally {
            session.close();
        }
    }
}
