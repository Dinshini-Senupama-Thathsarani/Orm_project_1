package lk.orm.project01.dao;

import lk.orm.project01.entity.TherapistProgramme;
import java.util.List;


public interface TherapistProgrammeDAO extends CrudDAO<TherapistProgramme, Long> {


    List<TherapistProgramme> findByTherapistId(String therapistId) throws Exception;


    List<TherapistProgramme> findByProgrammeId(String programmeId) throws Exception;
}
