package lk.orm.project01.dao;

import lk.orm.project01.entity.PatientProgramme;
import java.util.List;


public interface PatientProgrammeDAO extends CrudDAO<PatientProgramme, Long> {


    List<PatientProgramme> findByPatientId(String patientId) throws Exception;


    List<PatientProgramme> findByProgrammeId(String programmeId) throws Exception;


    boolean isEnrolled(String patientId, String programmeId) throws Exception;
}
