package lk.orm.project01.dao;

import lk.orm.project01.config.FactoryConfiguration;
import lk.orm.project01.entity.Register;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class RegisterDAOImpl implements RegisterDAO {

    // ---- CRUD Operations ----

    @Override
    public boolean save(Register entity) throws Exception {
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
    public boolean update(Register entity) throws Exception {
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
    public boolean delete(String username) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Register register = session.get(Register.class, username);
            if (register != null) {
                session.remove(register);
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
    public Register findById(String username) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            return session.get(Register.class, username);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Register> findAll() throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            // HQL query to retrieve all Register entities
            Query<Register> query = session.createQuery("FROM Register", Register.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    // ---- Custom Queries ----

    @Override
    public Register findByUsername(String username) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            // HQL: find user by username
            Query<Register> query = session.createQuery(
                "FROM Register r WHERE r.username = :username", Register.class
            );
            query.setParameter("username", username);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}
