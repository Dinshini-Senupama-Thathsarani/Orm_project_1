package lk.orm.project01.bo;

import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.PatientDAO;
import lk.orm.project01.dao.PatientProgrammeDAO;
import lk.orm.project01.dao.TherapyProgrammeDAO;
import lk.orm.project01.dto.PatientProgrammeDTO;
import lk.orm.project01.entity.Patient;
import lk.orm.project01.entity.PatientProgramme;
import lk.orm.project01.entity.TherapyProgramme;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;
import lk.orm.project01.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;


public class PatientProgrammeBOImpl implements PatientProgrammeBO {

    private final PatientProgrammeDAO ppDAO =
        (PatientProgrammeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PATIENT_PROGRAMME);
    private final PatientDAO patientDAO =
        (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PATIENT);
    private final TherapyProgrammeDAO programmeDAO =
        (TherapyProgrammeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPY_PROGRAMME);

    @Override
    public boolean enrollPatient(PatientProgrammeDTO dto)
            throws DuplicateEntryException, ValidationException, Exception {

        if (!ValidationUtil.isNotEmpty(dto.getPatientId())) {
            throw new ValidationException("Patient ID is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getProgrammeId())) {
            throw new ValidationException("Programme ID is required.");
        }

        // Check if already enrolled
        if (ppDAO.isEnrolled(dto.getPatientId(), dto.getProgrammeId())) {
            throw new DuplicateEntryException(
                "Patient " + dto.getPatientId() +
                " is already enrolled in programme " + dto.getProgrammeId() + "."
            );
        }

        Patient patient = patientDAO.findById(dto.getPatientId());
        if (patient == null) throw new Exception("Patient not found: " + dto.getPatientId());

        TherapyProgramme programme = programmeDAO.findById(dto.getProgrammeId());
        if (programme == null) throw new Exception("Programme not found: " + dto.getProgrammeId());

        PatientProgramme pp = new PatientProgramme(
            dto.getEnrolledDate(),
            dto.getStatus() != null ? dto.getStatus() : "ACTIVE",
            patient,
            programme
        );

        return ppDAO.save(pp);
    }

    @Override
    public boolean updateEnrollment(PatientProgrammeDTO dto) throws Exception {
        PatientProgramme pp = ppDAO.findById(dto.getId());
        if (pp == null) throw new Exception("Enrollment not found: " + dto.getId());
        pp.setStatus(dto.getStatus());
        pp.setEnrolledDate(dto.getEnrolledDate());
        return ppDAO.update(pp);
    }

    @Override
    public boolean removeEnrollment(Long id) throws Exception {
        return ppDAO.delete(id);
    }

    @Override
    public List<PatientProgrammeDTO> getEnrollmentsByPatient(String patientId) throws Exception {
        return convertToDTOList(ppDAO.findByPatientId(patientId));
    }

    @Override
    public List<PatientProgrammeDTO> getEnrollmentsByProgramme(String programmeId) throws Exception {
        return convertToDTOList(ppDAO.findByProgrammeId(programmeId));
    }

    @Override
    public List<PatientProgrammeDTO> getAllEnrollments() throws Exception {
        return convertToDTOList(ppDAO.findAll());
    }



    private PatientProgrammeDTO convertToDTO(PatientProgramme pp) {
        return new PatientProgrammeDTO(
            pp.getId(),
            pp.getEnrolledDate(),
            pp.getStatus(),
            pp.getPatient() != null ? pp.getPatient().getPatientId() : "",
            pp.getPatient() != null ? pp.getPatient().getName() : "",
            pp.getTherapyProgramme() != null ? pp.getTherapyProgramme().getProgrammeId() : "",
            pp.getTherapyProgramme() != null ? pp.getTherapyProgramme().getName() : "",
            pp.getTherapyProgramme() != null ? pp.getTherapyProgramme().getFee() : 0.0
        );
    }

    private List<PatientProgrammeDTO> convertToDTOList(List<PatientProgramme> list) {
        List<PatientProgrammeDTO> dtos = new ArrayList<>();
        for (PatientProgramme pp : list) dtos.add(convertToDTO(pp));
        return dtos;
    }
}
