package lk.orm.project01.bo;

import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.exception.InvalidCredentialsException;


public interface LoginBO extends SuperBO {

    /**
     * Authenticates a user with username and password.
     * Verifies BCrypt hash.
     *
     * @param username the entered username
     * @param password the plain-text password
     * @return RegisterDTO of the authenticated user
     * @throws InvalidCredentialsException if credentials are invalid
     * @throws Exception on other failures
     */
    RegisterDTO login(String username, String password)
            throws InvalidCredentialsException, Exception;

    /**
     * Changes the password for an existing user.
     *
     * @param username    the username
     * @param oldPassword the current plain-text password
     * @param newPassword the new plain-text password
     * @return true if changed successfully
     * @throws InvalidCredentialsException if old password is wrong
     * @throws Exception on other failures
     */
    boolean changePassword(String username, String oldPassword, String newPassword)
            throws InvalidCredentialsException, Exception;
}
