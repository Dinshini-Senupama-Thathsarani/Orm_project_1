package lk.orm.project01.dao;

import lk.orm.project01.entity.Patient;
import java.util.List;


public interface PatientDAO extends CrudDAO<Patient, String> {


    List<Patient> searchByName(String name) throws Exception;


    Patient findByNic(String nic) throws Exception;


    List<Patient> findPatientsEnrolledInAllProgrammes() throws Exception;


    List<Patient> findPatientsWithProgrammes() throws Exception;
}
