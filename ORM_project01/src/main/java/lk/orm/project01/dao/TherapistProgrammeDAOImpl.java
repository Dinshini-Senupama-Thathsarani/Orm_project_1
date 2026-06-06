package lk.orm.project01.dao;

import lk.orm.project01.config.FactoryConfiguration;
import lk.orm.project01.entity.TherapistProgramme;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class TherapistProgrammeDAOImpl implements TherapistProgrammeDAO {

    @Override
    public boolean save(TherapistProgramme entity) throws Exception {
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
    public boolean update(TherapistProgramme entity) throws Exception {
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
            TherapistProgramme tp = session.get(TherapistProgramme.class, id);
            if (tp != null) {
                session.remove(tp);
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
    public TherapistProgramme findById(Long id) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(TherapistProgramme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistProgramme> findAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<TherapistProgramme> query = session.createQuery(
                "FROM TherapistProgramme", TherapistProgramme.class
            );
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistProgramme> findByTherapistId(String therapistId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT tp FROM TherapistProgramme tp
                JOIN FETCH tp.therapyProgramme
                WHERE tp.therapist.therapistId = :therapistId
                """;
            Query<TherapistProgramme> query = session.createQuery(hql, TherapistProgramme.class);
            query.setParameter("therapistId", therapistId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistProgramme> findByProgrammeId(String programmeId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            String hql = """
                SELECT tp FROM TherapistProgramme tp
                JOIN FETCH tp.therapist
                WHERE tp.therapyProgramme.programmeId = :programmeId
                """;
            Query<TherapistProgramme> query = session.createQuery(hql, TherapistProgramme.class);
            query.setParameter("programmeId", programmeId);
            return query.list();
        } finally {
            session.close();
        }
    }
}
