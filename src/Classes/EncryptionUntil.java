package Classes;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

public class EncryptionUntil {

    public static final String ALGORITHM = "RSA";

    public static final String PRIVATE_KEY_FILE = "private.key";

    public static void generateKey() throws NoSuchAlgorithmException, IOException {

            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(4096);
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);

        ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                new FileOutputStream(privateKeyFile));
        privateKeyOS.writeObject(key);
        privateKeyOS.close();

    }


    public static KeyPair getKey() throws IOException, ClassNotFoundException {
        File publicKeyFile = new File(PRIVATE_KEY_FILE);
        KeyPair key = null;

        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(publicKeyFile));
        key = (KeyPair) stream.readObject();
        return key;
    }


    public static byte[] encrypt(String text) throws IOException, ClassNotFoundException {
        KeyPair key = getKey();
        byte[] cipherText = null;
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }


    public static String decrypt(byte[] text) throws IOException, ClassNotFoundException {
        KeyPair key = getKey();
        byte[] dectyptedText = null;
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new String(dectyptedText);
    }
}
