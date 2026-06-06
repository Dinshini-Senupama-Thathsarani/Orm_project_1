package lk.orm.project01.dao;

import lk.orm.project01.entity.Register;


public interface RegisterDAO extends CrudDAO<Register, String> {


    Register findByUsername(String username) throws Exception;
}
