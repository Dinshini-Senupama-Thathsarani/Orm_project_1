package lk.orm.project01.bo;

import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;

import java.util.List;


public interface RegisterBO extends SuperBO {

    /**
     * Registers a new system user (Admin or Receptionist).
     * Hashes the password before saving.
     *
     * @param dto the user data
     * @return true if registered successfully
     * @throws DuplicateEntryException if username already exists
     * @throws ValidationException     if input is invalid
     * @throws Exception               on other failures
     */
    boolean register(RegisterDTO dto)
            throws DuplicateEntryException, ValidationException, Exception;

    /**
     * Updates an existing user's details.
     *
     * @param dto the updated user data
     * @return true if updated successfully
     * @throws Exception on failure
     */
    boolean update(RegisterDTO dto) throws Exception;

    /**
     * Deletes a user account by username.
     *
     * @param username the username to delete
     * @return true if deleted
     * @throws Exception on failure
     */
    boolean delete(String username) throws Exception;

    /**
     * Retrieves all registered users.
     *
     * @return list of RegisterDTOs
     * @throws Exception on failure
     */
    List<RegisterDTO> getAllUsers() throws Exception;
}
