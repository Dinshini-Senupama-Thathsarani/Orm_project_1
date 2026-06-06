package lk.orm.project01.bo;

import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.TherapistDAO;
import lk.orm.project01.dto.TherapistDTO;
import lk.orm.project01.entity.Therapist;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;
import lk.orm.project01.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;


public class TherapistBOImpl implements TherapistBO {

    private final TherapistDAO therapistDAO =
        (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPIST);

    @Override
    public boolean saveTherapist(TherapistDTO dto)
            throws DuplicateEntryException, ValidationException, Exception {

        if (!ValidationUtil.isNotEmpty(dto.getTherapistId())) {
            throw new ValidationException("Therapist ID is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getName())) {
            throw new ValidationException("Therapist name is required.");
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()
                && !ValidationUtil.isValidPhone(dto.getPhone())) {
            throw new ValidationException("Invalid Sri Lankan phone number.");
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()
                && !ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new ValidationException("Invalid email address.");
        }

        // Check for duplicate ID
        if (therapistDAO.findById(dto.getTherapistId()) != null) {
            throw new DuplicateEntryException("Therapist ID '" + dto.getTherapistId() + "' already exists.");
        }

        return therapistDAO.save(convertToEntity(dto));
    }

    @Override
    public boolean updateTherapist(TherapistDTO dto)
            throws ValidationException, Exception {

        if (!ValidationUtil.isNotEmpty(dto.getName())) {
            throw new ValidationException("Therapist name is required.");
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()
                && !ValidationUtil.isValidPhone(dto.getPhone())) {
            throw new ValidationException("Invalid Sri Lankan phone number.");
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()
                && !ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new ValidationException("Invalid email address.");
        }

        return therapistDAO.update(convertToEntity(dto));
    }

    @Override
    public boolean deleteTherapist(String therapistId) throws Exception {
        return therapistDAO.delete(therapistId);
    }

    @Override
    public TherapistDTO getTherapistById(String therapistId) throws Exception {
        Therapist t = therapistDAO.findById(therapistId);
        return t != null ? convertToDTO(t) : null;
    }

    @Override
    public List<TherapistDTO> getAllTherapists() throws Exception {
        return convertToDTOList(therapistDAO.findAll());
    }

    @Override
    public List<TherapistDTO> searchTherapistsByName(String name) throws Exception {
        return convertToDTOList(therapistDAO.searchByName(name));
    }

    @Override
    public String generateNextTherapistId() throws Exception {
        List<Therapist> all = therapistDAO.findAll();
        if (all.isEmpty()) return "T001";
        int max = 0;
        for (Therapist t : all) {
            String id = t.getTherapistId();
            if (id != null && id.startsWith("T")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("T%03d", max + 1);
    }

    // ---- Conversion Helpers ----

    private Therapist convertToEntity(TherapistDTO dto) {
        return new Therapist(
            dto.getTherapistId(),
            dto.getName(),
            dto.getSpecialization(),
            dto.getPhone(),
            dto.getEmail(),
            dto.getAvailability()
        );
    }

    private TherapistDTO convertToDTO(Therapist t) {
        return new TherapistDTO(
            t.getTherapistId(),
            t.getName(),
            t.getSpecialization(),
            t.getPhone(),
            t.getEmail(),
            t.getAvailability()
        );
    }

    private List<TherapistDTO> convertToDTOList(List<Therapist> list) {
        List<TherapistDTO> dtos = new ArrayList<>();
        for (Therapist t : list) dtos.add(convertToDTO(t));
        return dtos;
    }
}
