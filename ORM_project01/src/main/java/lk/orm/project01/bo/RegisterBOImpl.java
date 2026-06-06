package lk.orm.project01.bo;

import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.RegisterDAO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.entity.Register;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;
import lk.orm.project01.util.PasswordUtil;
import lk.orm.project01.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;


public class RegisterBOImpl implements RegisterBO {

    private final RegisterDAO registerDAO =
        (RegisterDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.REGISTER);

    @Override
    public boolean register(RegisterDTO dto)
            throws DuplicateEntryException, ValidationException, Exception {


        if (!ValidationUtil.isNotEmpty(dto.getUsername())) {
            throw new ValidationException("Username is required.");
        }
        if (!ValidationUtil.isNotEmpty(dto.getPassword())) {
            throw new ValidationException("Password is required.");
        }
        if (!ValidationUtil.isValidPassword(dto.getPassword())) {
            throw new ValidationException(
                "Password must be at least 8 characters with uppercase, lowercase, digit, and special character."
            );
        }
        if (!ValidationUtil.isNotEmpty(dto.getRole())) {
            throw new ValidationException("Role is required.");
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()
                && !ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new ValidationException("Invalid email address.");
        }

        // ---- Check for duplicate username ----
        if (registerDAO.findByUsername(dto.getUsername()) != null) {
            throw new DuplicateEntryException("Username '" + dto.getUsername() + "' already exists.");
        }

        // ---- Hash password before saving ----
        String hashedPassword = PasswordUtil.hashPassword(dto.getPassword());

        Register register = new Register(
            dto.getUsername(),
            hashedPassword,
            dto.getRole(),
            dto.getEmail()
        );

        return registerDAO.save(register);
    }

    @Override
    public boolean update(RegisterDTO dto) throws Exception {
        Register register = registerDAO.findByUsername(dto.getUsername());
        if (register == null) {
            throw new Exception("User not found: " + dto.getUsername());
        }

        // Only update email and role (password changed via LoginBO.changePassword)
        if (dto.getEmail() != null) register.setEmail(dto.getEmail());
        if (dto.getRole() != null) register.setRole(dto.getRole());

        return registerDAO.update(register);
    }

    @Override
    public boolean delete(String username) throws Exception {
        return registerDAO.delete(username);
    }

    @Override
    public List<RegisterDTO> getAllUsers() throws Exception {
        List<Register> registers = registerDAO.findAll();
        List<RegisterDTO> dtos = new ArrayList<>();
        for (Register r : registers) {
            // Never expose hashed password in DTO
            dtos.add(new RegisterDTO(r.getUsername(), null, r.getRole(), r.getEmail()));
        }
        return dtos;
    }
}
