package auth;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Hasher un mot de passe
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Vérifier un mot de passe
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
