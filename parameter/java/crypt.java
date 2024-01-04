import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {

    private static final int BLOCK_SIZE = 32;

    public static byte[] pKCS7Padding(String plaintext, int blockSize) {
        int padding = blockSize - (plaintext.length() % blockSize);
        byte padValue = (byte) padding;
        byte[] padtext = new byte[padding];
        for (int i = 0; i < padding; i++) {
            padtext[i] = padValue;
        }
        return (plaintext + new String(padtext, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] pKCS7Unpadding(byte[] plaintext, int blockSize) {
        int padding = plaintext[plaintext.length - 1];
        if (padding > blockSize || padding > plaintext.length) {
            throw new IllegalArgumentException("Invalid padding");
        }
        return java.util.Arrays.copyOf(plaintext, plaintext.length - padding);
    }

    public static byte[] CbcEncrypt(String plaintext, String encodingAes) throws Exception {
        byte[] aeskey = Base64.getDecoder().decode(encodingAes + "=");
        int blockSize = 32;
        byte[] padMsg = pKCS7Padding(plaintext, blockSize);

        SecretKeySpec secretKeySpec = new SecretKeySpec(aeskey, "AES");
        byte[] iv = java.util.Arrays.copyOf(aeskey, 16);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] ciphertext = cipher.doFinal(padMsg);
        return Base64.getEncoder().encode(ciphertext);
    }

    public static byte[] CbcDecrypt(String base64EncryptMsg, String encodingAes) throws Exception {
        byte[] aeskey = Base64.getDecoder().decode(encodingAes + "=");
        byte[] encryptMsg = Base64.getDecoder().decode(base64EncryptMsg);
        int blockSize = 32;

        SecretKeySpec secretKeySpec = new SecretKeySpec(aeskey, "AES");
        byte[] iv = java.util.Arrays.copyOf(aeskey, 16);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decrypted = cipher.doFinal(encryptMsg);
        return pKCS7Unpadding(decrypted, blockSize);
    }

    public static void main(String[] args) {
        String plaintext = "1111";
        String encodingAes = "0VShOOAlYzVbKvcU0JRfsEdArPPunB5B6CMoFdAYVXa";

        try {
            byte[] secretText = CbcEncrypt(plaintext, encodingAes);
            System.out.println("密文：" + new String(secretText, StandardCharsets.UTF_8));

            byte[] text = CbcDecrypt(new String(secretText, StandardCharsets.UTF_8), encodingAes);
            System.out.println("明文：" + new String(text, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("加解密失败：" + e.getMessage());
        }
    }
}