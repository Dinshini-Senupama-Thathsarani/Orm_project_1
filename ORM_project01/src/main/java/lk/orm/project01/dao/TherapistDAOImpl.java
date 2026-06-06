package lk.orm.project01.dao;

import lk.orm.project01.config.FactoryConfiguration;
import lk.orm.project01.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class TherapistDAOImpl implements TherapistDAO {

    @Override
    public boolean save(Therapist entity) throws Exception {
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
    public boolean update(Therapist entity) throws Exception {
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
    public boolean delete(String therapistId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Therapist therapist = session.get(Therapist.class, therapistId);
            if (therapist != null) {
                session.remove(therapist);
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
    public Therapist findById(String therapistId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(Therapist.class, therapistId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Therapist> findAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<Therapist> query = session.createQuery("FROM Therapist ORDER BY name", Therapist.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Therapist> searchByName(String name) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<Therapist> query = session.createQuery(
                "FROM Therapist t WHERE LOWER(t.name) LIKE :name ORDER BY t.name",
                Therapist.class
            );
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            return query.list();
        } finally {
            session.close();
        }
    }

    /**
     * HQL: Retrieves therapists with their sessions using JOIN FETCH.
     * Avoids N+1 problem when loading session data.
     */
    @Override
    public List<Therapist> findTherapistsWithSessions() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT DISTINCT t FROM Therapist t
                LEFT JOIN FETCH t.therapySessions ts
                LEFT JOIN FETCH ts.patient
                ORDER BY t.name
                """;
            Query<Therapist> query = session.createQuery(hql, Therapist.class);
            return query.list();
        } finally {
            session.close();
        }
    }
}
