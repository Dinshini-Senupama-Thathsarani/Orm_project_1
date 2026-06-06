package lk.orm.project01.bo;

import lk.orm.project01.dto.TherapySessionDTO;
import lk.orm.project01.exception.SchedulingConflictException;
import lk.orm.project01.exception.ValidationException;

import java.util.List;


public interface TherapistSessionBO extends SuperBO {

    boolean bookSession(TherapySessionDTO dto)
            throws SchedulingConflictException, ValidationException, Exception;

    boolean updateSession(TherapySessionDTO dto)
            throws SchedulingConflictException, ValidationException, Exception;

    boolean cancelSession(String sessionId) throws Exception;

    TherapySessionDTO getSessionById(String sessionId) throws Exception;

    List<TherapySessionDTO> getAllSessions() throws Exception;

    List<TherapySessionDTO> getSessionsByTherapist(String therapistId) throws Exception;

    List<TherapySessionDTO> getSessionsByPatient(String patientId) throws Exception;

    List<TherapySessionDTO> getSessionsByStatus(String status) throws Exception;

    /** Generates the next session ID (e.g., S001, S002 ...) */
    String generateNextSessionId() throws Exception;
}
