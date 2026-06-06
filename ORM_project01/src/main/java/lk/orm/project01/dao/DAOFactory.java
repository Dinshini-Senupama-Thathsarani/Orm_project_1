package lk.orm.project01.dao;


public class DAOFactory {

    // ---- Singleton instance ----
    private static DAOFactory daoFactory;


    private DAOFactory() {}


    public static synchronized DAOFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }


    public enum DAOTypes {
        REGISTER,
        PATIENT,
        THERAPIST,
        THERAPY_PROGRAMME,
        THERAPIST_SESSION,
        PATIENT_PROGRAMME,
        THERAPIST_PROGRAMME,
        PAYMENT
    }


    public SuperDAO getDAO(DAOTypes daoType) {
        switch (daoType) {
            case REGISTER:
                return new RegisterDAOImpl();
            case PATIENT:
                return new PatientDAOImpl();
            case THERAPIST:
                return new TherapistDAOImpl();
            case THERAPY_PROGRAMME:
                return new TherapyProgrammeDAOImpl();
            case THERAPIST_SESSION:
                return new TherapistSessionDAOImpl();
            case PATIENT_PROGRAMME:
                return new PatientProgrammeDAOImpl();
            case THERAPIST_PROGRAMME:
                return new TherapistProgrammeDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            default:
                throw new IllegalArgumentException("Unknown DAO type: " + daoType);
        }
    }
}
