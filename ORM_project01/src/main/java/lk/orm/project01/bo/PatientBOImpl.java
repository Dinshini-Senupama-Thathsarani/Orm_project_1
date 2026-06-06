package lk.orm.project01.bo;

import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.PatientDAO;
import lk.orm.project01.dto.PatientDTO;
import lk.orm.project01.entity.Patient;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;
import lk.orm.project01.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;


public class PatientBOImpl implements PatientBO {

    private final PatientDAO patientDAO =
        (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PATIENT);

    @Override
    public boolean savePatient(PatientDTO dto)
            throws DuplicateEntryException, ValidationException, Exception {


        if (!ValidationUtil.isNotEmpty(dto.getPatientId())) {
            throw new ValidationException("Patient ID is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getName())) {
            throw new ValidationException("Patient name is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getNic())) {
            throw new ValidationException("NIC is required.");
        }
        if (!ValidationUtil.isValidNIC(dto.getNic())) {
            throw new ValidationException("Invalid NIC format. Use 9 digits + V/X or 12 digits.");
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()
                && !ValidationUtil.isValidPhone(dto.getPhone())) {
            throw new ValidationException("Invalid Sri Lankan phone number.");
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()
                && !ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new ValidationException("Invalid email address.");
        }

        // ---- Check for duplicate patient ID ----
        if (patientDAO.findById(dto.getPatientId()) != null) {
            throw new DuplicateEntryException("Patient ID '" + dto.getPatientId() + "' already exists.");
        }

        // ---- Check for duplicate NIC ----
        if (patientDAO.findByNic(dto.getNic()) != null) {
            throw new DuplicateEntryException("A patient with NIC '" + dto.getNic() + "' already exists.");
        }

        Patient patient = convertToEntity(dto);
        return patientDAO.save(patient);
    }

    @Override
    public boolean updatePatient(PatientDTO dto)
            throws ValidationException, Exception {

        if (!ValidationUtil.isNotEmpty(dto.getName())) {
            throw new ValidationException("Patient name is required.");
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()
                && !ValidationUtil.isValidPhone(dto.getPhone())) {
            throw new ValidationException("Invalid Sri Lankan phone number.");
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()
                && !ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new ValidationException("Invalid email address.");
        }

        Patient patient = convertToEntity(dto);
        return patientDAO.update(patient);
    }

    @Override
    public boolean deletePatient(String patientId) throws Exception {
        return patientDAO.delete(patientId);
    }

    @Override
    public PatientDTO getPatientById(String patientId) throws Exception {
        Patient patient = patientDAO.findById(patientId);
        return patient != null ? convertToDTO(patient) : null;
    }

    @Override
    public List<PatientDTO> getAllPatients() throws Exception {
        List<Patient> patients = patientDAO.findAll();
        return convertToDTOList(patients);
    }

    @Override
    public List<PatientDTO> searchPatientsByName(String name) throws Exception {
        List<Patient> patients = patientDAO.searchByName(name);
        return convertToDTOList(patients);
    }

    @Override
    public List<PatientDTO> getPatientsEnrolledInAllProgrammes() throws Exception {
        List<Patient> patients = patientDAO.findPatientsEnrolledInAllProgrammes();
        return convertToDTOList(patients);
    }

    @Override
    public String generateNextPatientId() throws Exception {
        List<Patient> all = patientDAO.findAll();
        if (all.isEmpty()) return "P001";

        // Find the highest numeric suffix
        int max = 0;
        for (Patient p : all) {
            String id = p.getPatientId();
            if (id != null && id.startsWith("P")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("P%03d", max + 1);
    }

    // ---- Conversion Helpers ----

    private Patient convertToEntity(PatientDTO dto) {
        return new Patient(
            dto.getPatientId(),
            dto.getName(),
            dto.getNic(),
            dto.getDob(),
            dto.getGender(),
            dto.getAddress(),
            dto.getPhone(),
            dto.getEmail(),
            dto.getMedicalHistory()
        );
    }

    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
            patient.getPatientId(),
            patient.getName(),
            patient.getNic(),
            patient.getDob(),
            patient.getGender(),
            patient.getAddress(),
            patient.getPhone(),
            patient.getEmail(),
            patient.getMedicalHistory()
        );
    }

    private List<PatientDTO> convertToDTOList(List<Patient> patients) {
        List<PatientDTO> dtos = new ArrayList<>();
        for (Patient p : patients) {
            dtos.add(convertToDTO(p));
        }
        return dtos;
    }
}
