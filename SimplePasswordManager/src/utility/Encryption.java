package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encryption {

   public static byte[] getKey(char[] password) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
      File fkey = new File("TOTS_NOT_THE_KEY_GO_AWAY.txt");
      if (fkey.exists()) {
         return Files.readAllBytes(Paths.get(fkey.getAbsolutePath()));
      }

      // key from password and salt
      byte[] salt = new byte[16];
      new SecureRandom().nextBytes(salt);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
      fkey.createNewFile();
      Files.write(Paths.get(fkey.getAbsolutePath()), secret.getEncoded());

      return secret.getEncoded();
   }

   public static String encrypt(byte[] key, String initVector, String value) {
      try {
         SecretKey secret = new SecretKeySpec(key, "AES");
         IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));

         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
         cipher.init(Cipher.ENCRYPT_MODE, secret, iv);

         byte[] encrypted = cipher.doFinal(value.getBytes());

         return Base64.encodeBase64String(encrypted);
      } catch (Exception ex) {
         ex.printStackTrace();
      }

      return null;
   }

   public static String decrypt(byte[] key, String initVector, String encrypted) {
      try {
         SecretKey secret = new SecretKeySpec(key, "AES");
         IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));

         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
         cipher.init(Cipher.DECRYPT_MODE, secret, iv);

         byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

         return new String(original);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return null;
   }

   public static String getInitVector() {
      File fp = new File("initvector.txt");
      String initVector = null;
      byte[] data;

      if (fp.exists()) {
         try {
            FileInputStream fis = new FileInputStream(fp);
            data = new byte[(int) fp.length()];
            fis.read(data);
            fis.close();
            initVector = new String(data, "UTF-8");
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
      } else {
         initVector = PasswordGenerator.generateStrongPassword(16);
         try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fp));
            out.write(initVector);
            out.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

      return initVector;
   }
}