package com.meicode.hashmessage.utils;

import static java.util.Base64.*;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CipherUtils {
    private static final String PASSWORD ="THor HAV HAMM35";
    private static final String ALGORITHM ="DES";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptIt(@NonNull String value){

        try {
            DESKeySpec keySpec = new DESKeySpec(PASSWORD.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(keySpec);
            byte[] clearText = value.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            Base64.getEncoder().encodeToString(cipher.doFinal(clearText));

        }
        catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e){
            e.printStackTrace();
        }

        return value;
    }  // encryptIt

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static String decryptIt(@NonNull String value){
        try {
            DESKeySpec keySpec = new DESKeySpec(PASSWORD.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encryptedPwdBytes = Base64.getDecoder().decode(value);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] decryptedValueBytes = (cipher.doFinal(encryptedPwdBytes));
            return new String(decryptedValueBytes);

        } catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return value;
        } // decryptIt


} // CipherUtils


// return Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
// byte[] encryptedPwdBytes = Base64.decode(value, Base64.);