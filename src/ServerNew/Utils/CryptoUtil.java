package ServerNew.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class CryptoUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    public static final String secretKey = "abcdefghijklmnop"; // Key mặc định

    // Chuẩn hóa khóa AES (16 bytes)
    public static String getValidKey(String key) throws Exception {
        byte[] keyBytes = key.getBytes("UTF-8");
        if (keyBytes.length < 16) {
            keyBytes = Arrays.copyOf(keyBytes, 16);
        } else if (keyBytes.length > 16) {
            keyBytes = Arrays.copyOf(keyBytes, 16); // Nếu khóa dài hơn 16 bytes, cắt bớt
        }
        return Base64.getEncoder().encodeToString(keyBytes);
    }


    // Mã hóa dữ liệu
    public static String encrypt(String key, String data) throws Exception {
        String validKey = getValidKey(key); // Gọi getValidKey để chuẩn hóa khóa
        byte[] decodedKey = Base64.getDecoder().decode(validKey); // Sử dụng khóa đã chuẩn hóa
        SecretKeySpec secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Giải mã dữ liệu
    public static String decrypt(String key, String encryptedData) throws Exception {
        String validKey = getValidKey(key); // Gọi getValidKey để chuẩn hóa khóa
        byte[] decodedKey = Base64.getDecoder().decode(validKey); // Sử dụng khóa đã chuẩn hóa
        SecretKeySpec secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
