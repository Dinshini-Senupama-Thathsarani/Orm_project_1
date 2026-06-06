package lk.orm.project01.util;

import java.util.regex.Pattern;


public class ValidationUtil {


    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");


    private static final Pattern SL_PHONE_PATTERN =
        Pattern.compile("^(\\+94|0)[0-9]{9}$");


    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");


    private static final Pattern NIC_PATTERN =
        Pattern.compile("^([0-9]{9}[vVxX]|[0-9]{12})$");


    private static final Pattern ID_PATTERN =
        Pattern.compile("^[A-Za-z0-9]+$");


    private ValidationUtil() {}




    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }


    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return SL_PHONE_PATTERN.matcher(phone.trim()).matches();
    }


    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) return false;
        return PASSWORD_PATTERN.matcher(password).matches();
    }


    public static boolean isValidNIC(String nic) {
        if (nic == null || nic.trim().isEmpty()) return false;
        return NIC_PATTERN.matcher(nic.trim()).matches();
    }


    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }


    public static boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return ID_PATTERN.matcher(id.trim()).matches();
    }


    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
}
