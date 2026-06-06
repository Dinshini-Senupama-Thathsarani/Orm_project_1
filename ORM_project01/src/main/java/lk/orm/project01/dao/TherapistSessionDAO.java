package lk.orm.project01.dao;

import lk.orm.project01.entity.TherapySession;
import java.util.List;


public interface TherapistSessionDAO extends CrudDAO<TherapySession, String> {


    List<TherapySession> findByTherapistId(String therapistId) throws Exception;


    List<TherapySession> findByPatientId(String patientId) throws Exception;


    boolean hasConflict(String therapistId, String sessionDate,
                        String sessionTime, String excludeSessionId) throws Exception;


    List<TherapySession> findByStatus(String status) throws Exception;
}
