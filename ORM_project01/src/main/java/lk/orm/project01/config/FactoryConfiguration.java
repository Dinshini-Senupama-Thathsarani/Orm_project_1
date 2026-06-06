package lk.orm.project01.config;

import lk.orm.project01.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;


public class FactoryConfiguration {

    private static FactoryConfiguration factoryConfiguration;


    private final SessionFactory sessionFactory;


    private FactoryConfiguration() {

        Properties properties = new Properties();
        try {
            properties.load(
                FactoryConfiguration.class.getResourceAsStream("/hib.properties")
            );
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to load hib.properties: " + e.getMessage());
        }


        Configuration configuration = new Configuration();
        configuration.setProperties(properties);


        configuration.addAnnotatedClass(Register.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Therapist.class);
        configuration.addAnnotatedClass(TherapyProgramme.class);
        configuration.addAnnotatedClass(TherapySession.class);
        configuration.addAnnotatedClass(PatientProgramme.class);
        configuration.addAnnotatedClass(TherapistProgramme.class);
        configuration.addAnnotatedClass(Payment.class);


        ServiceRegistry serviceRegistry =
            new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }


    public static synchronized FactoryConfiguration getInstance() {
        if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }


    public Session getSession() {
        return sessionFactory.openSession();
    }


    public void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
