package lk.orm.project01.dao;

import lk.orm.project01.entity.Therapist;
import java.util.List;


public interface TherapistDAO extends CrudDAO<Therapist, String> {


    List<Therapist> searchByName(String name) throws Exception;


    List<Therapist> findTherapistsWithSessions() throws Exception;
}
