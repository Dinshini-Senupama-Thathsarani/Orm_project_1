package lk.orm.project01.bo;

import lk.orm.project01.dto.TherapistDTO;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;

import java.util.List;


public interface TherapistBO extends SuperBO {

    boolean saveTherapist(TherapistDTO dto)
            throws DuplicateEntryException, ValidationException, Exception;

    boolean updateTherapist(TherapistDTO dto)
            throws ValidationException, Exception;

    boolean deleteTherapist(String therapistId) throws Exception;

    TherapistDTO getTherapistById(String therapistId) throws Exception;

    List<TherapistDTO> getAllTherapists() throws Exception;

    List<TherapistDTO> searchTherapistsByName(String name) throws Exception;

    /** Generates the next therapist ID (e.g., T001, T002 ...) */
    String generateNextTherapistId() throws Exception;
}
