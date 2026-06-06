package lk.orm.project01.util;

import org.mindrot.jbcrypt.BCrypt;


public class PasswordUtil {


    private static final int BCRYPT_ROUNDS = 12;


    private PasswordUtil() {}


    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        String salt = BCrypt.gensalt(BCRYPT_ROUNDS);
        return BCrypt.hashpw(plainPassword, salt);
    }


    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
