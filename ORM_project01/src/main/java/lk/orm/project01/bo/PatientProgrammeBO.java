package lk.orm.project01.bo;

import lk.orm.project01.dto.PatientProgrammeDTO;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;

import java.util.List;

public interface PatientProgrammeBO extends SuperBO {

    boolean enrollPatient(PatientProgrammeDTO dto)
            throws DuplicateEntryException, ValidationException, Exception;

    boolean updateEnrollment(PatientProgrammeDTO dto) throws Exception;

    boolean removeEnrollment(Long id) throws Exception;

    List<PatientProgrammeDTO> getEnrollmentsByPatient(String patientId) throws Exception;

    List<PatientProgrammeDTO> getEnrollmentsByProgramme(String programmeId) throws Exception;

    List<PatientProgrammeDTO> getAllEnrollments() throws Exception;
}
