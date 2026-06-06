package lk.orm.project01.bo;

import lk.orm.project01.dto.PatientDTO;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;

import java.util.List;

public interface PatientBO extends SuperBO {

    boolean savePatient(PatientDTO dto)
            throws DuplicateEntryException, ValidationException, Exception;

    boolean updatePatient(PatientDTO dto)
            throws ValidationException, Exception;

    boolean deletePatient(String patientId) throws Exception;

    PatientDTO getPatientById(String patientId) throws Exception;

    List<PatientDTO> getAllPatients() throws Exception;

    List<PatientDTO> searchPatientsByName(String name) throws Exception;

    /** Returns patients enrolled in ALL therapy programmes (HQL requirement). */
    List<PatientDTO> getPatientsEnrolledInAllProgrammes() throws Exception;

    /** Generates the next patient ID (e.g., P001, P002 ...) */
    String generateNextPatientId() throws Exception;
}
