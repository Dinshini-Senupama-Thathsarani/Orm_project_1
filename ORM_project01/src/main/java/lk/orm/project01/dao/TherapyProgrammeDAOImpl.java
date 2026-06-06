package lk.orm.project01.dao;

import lk.orm.project01.config.FactoryConfiguration;
import lk.orm.project01.entity.TherapyProgramme;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class TherapyProgrammeDAOImpl implements TherapyProgrammeDAO {

    @Override
    public boolean save(TherapyProgramme entity) throws Exception {
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
    public boolean update(TherapyProgramme entity) throws Exception {
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
    public boolean delete(String programmeId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            TherapyProgramme programme = session.get(TherapyProgramme.class, programmeId);
            if (programme != null) {
                session.remove(programme);
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
    public TherapyProgramme findById(String programmeId) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(TherapyProgramme.class, programmeId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapyProgramme> findAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Query<TherapyProgramme> query = session.createQuery(
                "FROM TherapyProgramme ORDER BY programmeId", TherapyProgramme.class
            );
            return query.list();
        } finally {
            session.close();
        }
    }
}
