package lk.orm.project01.bo;

import lk.orm.project01.dto.TherapyProgrammeDTO;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;

import java.util.List;


public interface TherapyProgrammeBO extends SuperBO {

    boolean saveProgramme(TherapyProgrammeDTO dto)
            throws DuplicateEntryException, ValidationException, Exception;

    boolean updateProgramme(TherapyProgrammeDTO dto)
            throws ValidationException, Exception;

    boolean deleteProgramme(String programmeId) throws Exception;

    TherapyProgrammeDTO getProgrammeById(String programmeId) throws Exception;

    List<TherapyProgrammeDTO> getAllProgrammes() throws Exception;
}
