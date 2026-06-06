package lk.orm.project01.bo;

import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.RegisterDAO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.entity.Register;
import lk.orm.project01.exception.InvalidCredentialsException;
import lk.orm.project01.util.PasswordUtil;
import lk.orm.project01.util.ValidationUtil;

/**
 * LoginBOImpl — Business logic implementation for authentication.
 *
 * Responsibilities:
 *  - Validate input fields
 *  - Verify BCrypt password hash
 *  - Return user DTO on success
 */
public class LoginBOImpl implements LoginBO {

    // Obtain RegisterDAO via factory
    private final RegisterDAO registerDAO =
        (RegisterDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.REGISTER);

    @Override
    public RegisterDTO login(String username, String password)
            throws InvalidCredentialsException, Exception {

        // ---- Input validation ----
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password)) {
            throw new InvalidCredentialsException("Username and password are required.");
        }

        // ---- Fetch user from DB ----
        Register register = registerDAO.findByUsername(username.trim());

        if (register == null) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        // ---- BCrypt password verification ----
        if (!PasswordUtil.checkPassword(password, register.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        // ---- Return DTO (never expose the hashed password) ----
        return new RegisterDTO(
            register.getUsername(),
            null,           // password not returned
            register.getRole(),
            register.getEmail()
        );
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword)
            throws InvalidCredentialsException, Exception {

        if (!ValidationUtil.isNotEmpty(username)
                || !ValidationUtil.isNotEmpty(oldPassword)
                || !ValidationUtil.isNotEmpty(newPassword)) {
            throw new InvalidCredentialsException("All fields are required.");
        }

        // Validate new password strength
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new InvalidCredentialsException(
                "New password must be at least 8 characters and include uppercase, " +
                "lowercase, digit, and special character."
            );
        }

        Register register = registerDAO.findByUsername(username);
        if (register == null) {
            throw new InvalidCredentialsException("User not found.");
        }

        // Verify old password
        if (!PasswordUtil.checkPassword(oldPassword, register.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect.");
        }

        // Hash and save new password
        register.setPassword(PasswordUtil.hashPassword(newPassword));
        return registerDAO.update(register);
    }
}
