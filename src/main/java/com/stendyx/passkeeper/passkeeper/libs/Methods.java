package com.stendyx.passkeeper.passkeeper.libs;

import com.stendyx.passkeeper.passkeeper.Main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Methods {

    /**
     * Hash string
     *
     * @param stringToHash String
     * @return String
     */
    public static String hashString(String stringToHash) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(stringToHash.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02X", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
        return "";
    }

}
