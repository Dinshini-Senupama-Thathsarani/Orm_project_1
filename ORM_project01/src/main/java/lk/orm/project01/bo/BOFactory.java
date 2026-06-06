package lk.orm.project01.bo;


public class BOFactory {


    private static BOFactory boFactory;


    private BOFactory() {}


    public static synchronized BOFactory getInstance() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }


    public enum BOTypes {
        LOGIN,
        REGISTER,
        PATIENT,
        THERAPIST,
        THERAPY_PROGRAMME,
        THERAPIST_SESSION,
        PATIENT_PROGRAMME,
        PAYMENT
    }


    public SuperBO getBO(BOTypes boType) {
        switch (boType) {
            case LOGIN:
                return new LoginBOImpl();
            case REGISTER:
                return new RegisterBOImpl();
            case PATIENT:
                return new PatientBOImpl();
            case THERAPIST:
                return new TherapistBOImpl();
            case THERAPY_PROGRAMME:
                return new TherapyProgrammeBOImpl();
            case THERAPIST_SESSION:
                return new TherapistSessionBOImpl();
            case PATIENT_PROGRAMME:
                return new PatientProgrammeBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            default:
                throw new IllegalArgumentException("Unknown BO type: " + boType);
        }
    }
}
