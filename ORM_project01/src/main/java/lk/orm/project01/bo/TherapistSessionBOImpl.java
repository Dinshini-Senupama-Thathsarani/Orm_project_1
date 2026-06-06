package lk.orm.project01.bo;

import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.PatientDAO;
import lk.orm.project01.dao.TherapistDAO;
import lk.orm.project01.dao.TherapistSessionDAO;
import lk.orm.project01.dao.TherapyProgrammeDAO;
import lk.orm.project01.dto.TherapySessionDTO;
import lk.orm.project01.entity.Patient;
import lk.orm.project01.entity.Therapist;
import lk.orm.project01.entity.TherapyProgramme;
import lk.orm.project01.entity.TherapySession;
import lk.orm.project01.exception.SchedulingConflictException;
import lk.orm.project01.exception.ValidationException;
import lk.orm.project01.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class TherapistSessionBOImpl implements TherapistSessionBO {

    private final TherapistSessionDAO sessionDAO =
        (TherapistSessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPIST_SESSION);
    private final PatientDAO patientDAO =
        (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PATIENT);
    private final TherapistDAO therapistDAO =
        (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPIST);
    private final TherapyProgrammeDAO programmeDAO =
        (TherapyProgrammeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPY_PROGRAMME);

    @Override
    public boolean bookSession(TherapySessionDTO dto)
            throws SchedulingConflictException, ValidationException, Exception {

        validateSessionDTO(dto);


        if (sessionDAO.hasConflict(dto.getTherapistId(), dto.getSessionDate(),
                dto.getSessionTime(), null)) {
            throw new SchedulingConflictException(
                "Therapist " + dto.getTherapistId() +
                " already has a session on " + dto.getSessionDate() +
                " at " + dto.getSessionTime() + "."
            );
        }

        TherapySession session = buildEntity(dto);
        return sessionDAO.save(session);
    }

    @Override
    public boolean updateSession(TherapySessionDTO dto)
            throws SchedulingConflictException, ValidationException, Exception {

        validateSessionDTO(dto);


        if (sessionDAO.hasConflict(dto.getTherapistId(), dto.getSessionDate(),
                dto.getSessionTime(), dto.getSessionId())) {
            throw new SchedulingConflictException(
                "Scheduling conflict: Therapist " + dto.getTherapistId() +
                " already has a session on " + dto.getSessionDate() +
                " at " + dto.getSessionTime() + "."
            );
        }

        TherapySession session = buildEntity(dto);
        return sessionDAO.update(session);
    }

    @Override
    public boolean cancelSession(String sessionId) throws Exception {
        TherapySession session = sessionDAO.findById(sessionId);
        if (session == null) throw new Exception("Session not found: " + sessionId);
        session.setStatus("CANCELLED");
        return sessionDAO.update(session);
    }

    @Override
    public TherapySessionDTO getSessionById(String sessionId) throws Exception {
        TherapySession s = sessionDAO.findById(sessionId);
        return s != null ? convertToDTO(s) : null;
    }

    @Override
    public List<TherapySessionDTO> getAllSessions() throws Exception {
        return convertToDTOList(sessionDAO.findAll());
    }

    @Override
    public List<TherapySessionDTO> getSessionsByTherapist(String therapistId) throws Exception {
        return convertToDTOList(sessionDAO.findByTherapistId(therapistId));
    }

    @Override
    public List<TherapySessionDTO> getSessionsByPatient(String patientId) throws Exception {
        return convertToDTOList(sessionDAO.findByPatientId(patientId));
    }

    @Override
    public List<TherapySessionDTO> getSessionsByStatus(String status) throws Exception {
        return convertToDTOList(sessionDAO.findByStatus(status));
    }

    @Override
    public String generateNextSessionId() throws Exception {
        List<TherapySession> all = sessionDAO.findAll();
        if (all.isEmpty()) return "S001";
        int max = 0;
        for (TherapySession s : all) {
            String id = s.getSessionId();
            if (id != null && id.startsWith("S")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("S%03d", max + 1);
    }



    private void validateSessionDTO(TherapySessionDTO dto) throws ValidationException {
        if (!ValidationUtil.isNotEmpty(dto.getSessionId())) {
            throw new ValidationException("Session ID is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getSessionDate())) {
            throw new ValidationException("Session date is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getSessionTime())) {
            throw new ValidationException("Session time is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getPatientId())) {
            throw new ValidationException("Patient is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getTherapistId())) {
            throw new ValidationException("Therapist is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getProgrammeId())) {
            throw new ValidationException("Therapy programme is required.");
        }
    }



    private TherapySession buildEntity(TherapySessionDTO dto) throws Exception {
        Patient patient = patientDAO.findById(dto.getPatientId());
        if (patient == null) throw new Exception("Patient not found: " + dto.getPatientId());

        Therapist therapist = therapistDAO.findById(dto.getTherapistId());
        if (therapist == null) throw new Exception("Therapist not found: " + dto.getTherapistId());

        TherapyProgramme programme = programmeDAO.findById(dto.getProgrammeId());
        if (programme == null) throw new Exception("Programme not found: " + dto.getProgrammeId());

        return new TherapySession(
            dto.getSessionId(),
            dto.getSessionDate(),
            dto.getSessionTime(),
            dto.getStatus() != null ? dto.getStatus() : "SCHEDULED",
            dto.getNotes(),
            patient,
            therapist,
            programme
        );
    }

    // ---- Conversion Helpers ----

    private TherapySessionDTO convertToDTO(TherapySession s) {
        return new TherapySessionDTO(
            s.getSessionId(),
            s.getSessionDate(),
            s.getSessionTime(),
            s.getStatus(),
            s.getNotes(),
            s.getPatient() != null ? s.getPatient().getPatientId() : "",
            s.getPatient() != null ? s.getPatient().getName() : "",
            s.getTherapist() != null ? s.getTherapist().getTherapistId() : "",
            s.getTherapist() != null ? s.getTherapist().getName() : "",
            s.getTherapyProgramme() != null ? s.getTherapyProgramme().getProgrammeId() : "",
            s.getTherapyProgramme() != null ? s.getTherapyProgramme().getName() : ""
        );
    }

    private List<TherapySessionDTO> convertToDTOList(List<TherapySession> list) {
        List<TherapySessionDTO> dtos = new ArrayList<>();
        for (TherapySession s : list) dtos.add(convertToDTO(s));
        return dtos;
    }
}
