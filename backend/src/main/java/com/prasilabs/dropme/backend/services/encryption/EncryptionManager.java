package com.prasilabs.dropme.backend.services.encryption;

import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.util.DataUtil;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionManager {
    private static final String key = "SNkngF8!"; // 128 bit key
    private static final String TAG = EncryptionManager.class.getSimpleName();

    public static String encryptData(String data) {
        try {
            if (data != null && data.length() > 0) {
                // Create key and cipher
                Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                // encrypt the text
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                byte[] encrypted = cipher.doFinal(data.getBytes());

                StringBuilder sb = new StringBuilder();
                for (byte b : encrypted) {
                    sb.append((char) b);
                }

                // the encrypted String
                String enc = sb.toString();
                ConsoleLog.i(TAG, "encrypted:" + enc);

                return enc;
                // now convert the string to byte array
                // for decryption
            }
        } catch (Exception e) {
            ConsoleLog.e(e);
        }

        return null;
    }

    public static String decryptData(String data) {
        try {
            if (!DataUtil.isEmpty(data)) {
                byte[] bb = new byte[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    bb[i] = (byte) data.charAt(i);
                }

                Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                // decrypt the text
                cipher.init(Cipher.DECRYPT_MODE, aesKey);
                String decrypted = new String(cipher.doFinal(bb));
                ConsoleLog.i(TAG, "decrypted:" + decrypted);

                return decrypted;
            }
        } catch (Exception e) {
            ConsoleLog.e(e);
        }

        return null;
    }
}