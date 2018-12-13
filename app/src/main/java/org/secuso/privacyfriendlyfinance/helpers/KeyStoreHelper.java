package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

public class KeyStoreHelper {
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String RSA_MODE =  "RSA/ECB/PKCS1Padding";
    private KeyStore keyStore;
    private String keyAlias;
    private KeyStore.PrivateKeyEntry privateKeyEntry;

    public KeyStoreHelper(String keyAlias) throws Exception {
        this.keyAlias = keyAlias;
        keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        try {
            privateKeyEntry = getKey();
        } catch (UnrecoverableKeyException ex) {
//            deleteKey();
        }
    }

    public boolean keyExists() {
        return privateKeyEntry != null;
    }

    public boolean deleteKey() throws Exception {
        if (keyStore.containsAlias(keyAlias)) {
            keyStore.deleteEntry(keyAlias);
            return true;
        }
        return false;
    }

    public void generateKey(Context context) throws Exception {
        deleteKey();
        // Generate a key pair for encryption
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);
        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                .setAlias(keyAlias)
                .setSubject(new X500Principal("CN=" + keyAlias))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA);
        kpg.initialize(spec);
        kpg.generateKeyPair();
        privateKeyEntry = getKey();
    }

    // from https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
    public byte[] rsaEncrypt(byte[] secret) throws Exception {
        // Encrypt the text
        Cipher inputCipher = Cipher.getInstance(RSA_MODE);
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        byte[] vals = outputStream.toByteArray();
        return vals;
    }

    private final int GETKEY_RETRY_COUNT = 3;
    private KeyStore.PrivateKeyEntry getKey() throws Exception {
        return getKey(0);
    }

    private KeyStore.PrivateKeyEntry getKey(int tryCounter) throws Exception {
        if (!keyStore.containsAlias(keyAlias)) return null;
        try {
            return (KeyStore.PrivateKeyEntry)keyStore.getEntry(keyAlias, null);
        } catch (UnrecoverableKeyException ex) {
            if (tryCounter < GETKEY_RETRY_COUNT) {
                return getKey(tryCounter + 1);
            } else {
                throw ex;
            }
        } catch (final Exception e) {
            throw e;
        }
    }

    // from https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
    public byte[] rsaDecrypt(byte[] encrypted) throws Exception {
        Cipher output = Cipher.getInstance(RSA_MODE);
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(encrypted), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte)nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }
        return bytes;
    }

    public String createPassphrase() throws Exception {
        byte[] key = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        byte[] encryptedKey = rsaEncrypt(key);
        return Base64.encodeToString(encryptedKey, Base64.DEFAULT);
    }
}
