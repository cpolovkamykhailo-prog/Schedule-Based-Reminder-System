package com.antigravity.reminder.infrastructure;

import org.mindrot.jbcrypt.BCrypt;

/** Service for secure password hashing using BCrypt. */
public class HashingService {
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verify(String password, String hashed) {
        try {
            return BCrypt.checkpw(password, hashed);
        } catch (Exception e) {
            return false;
        }
    }
}
