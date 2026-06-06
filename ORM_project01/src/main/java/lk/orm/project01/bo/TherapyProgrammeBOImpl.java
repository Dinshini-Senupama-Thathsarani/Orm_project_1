package lk.orm.project01.bo;

import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.TherapyProgrammeDAO;
import lk.orm.project01.dto.TherapyProgrammeDTO;
import lk.orm.project01.entity.TherapyProgramme;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;
import lk.orm.project01.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;


public class TherapyProgrammeBOImpl implements TherapyProgrammeBO {

    private final TherapyProgrammeDAO programmeDAO =
        (TherapyProgrammeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPY_PROGRAMME);

    @Override
    public boolean saveProgramme(TherapyProgrammeDTO dto)
            throws DuplicateEntryException, ValidationException, Exception {

        if (!ValidationUtil.isNotEmpty(dto.getProgrammeId())) {
            throw new ValidationException("Programme ID is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getName())) {
            throw new ValidationException("Programme name is required.");
        }
        if (!ValidationUtil.isValidAmount(dto.getFee())) {
            throw new ValidationException("Programme fee must be a positive value.");
        }
        if (programmeDAO.findById(dto.getProgrammeId()) != null) {
            throw new DuplicateEntryException("Programme ID '" + dto.getProgrammeId() + "' already exists.");
        }

        return programmeDAO.save(convertToEntity(dto));
    }

    @Override
    public boolean updateProgramme(TherapyProgrammeDTO dto)
            throws ValidationException, Exception {

        if (!ValidationUtil.isNotEmpty(dto.getName())) {
            throw new ValidationException("Programme name is required.");
        }
        if (!ValidationUtil.isValidAmount(dto.getFee())) {
            throw new ValidationException("Programme fee must be a positive value.");
        }

        return programmeDAO.update(convertToEntity(dto));
    }

    @Override
    public boolean deleteProgramme(String programmeId) throws Exception {
        return programmeDAO.delete(programmeId);
    }

    @Override
    public TherapyProgrammeDTO getProgrammeById(String programmeId) throws Exception {
        TherapyProgramme p = programmeDAO.findById(programmeId);
        return p != null ? convertToDTO(p) : null;
    }

    @Override
    public List<TherapyProgrammeDTO> getAllProgrammes() throws Exception {
        return convertToDTOList(programmeDAO.findAll());
    }



    private TherapyProgramme convertToEntity(TherapyProgrammeDTO dto) {
        return new TherapyProgramme(
            dto.getProgrammeId(),
            dto.getName(),
            dto.getDescription(),
            dto.getDuration(),
            dto.getFee()
        );
    }

    private TherapyProgrammeDTO convertToDTO(TherapyProgramme p) {
        return new TherapyProgrammeDTO(
            p.getProgrammeId(),
            p.getName(),
            p.getDescription(),
            p.getDuration(),
            p.getFee()
        );
    }

    private List<TherapyProgrammeDTO> convertToDTOList(List<TherapyProgramme> list) {
        List<TherapyProgrammeDTO> dtos = new ArrayList<>();
        for (TherapyProgramme p : list) dtos.add(convertToDTO(p));
        return dtos;
    }
}
