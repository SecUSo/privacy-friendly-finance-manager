/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

/**
 * Helper class to manage database keys.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class KeyStoreHelper {
    private static KeyStoreHelper singletonInstance;

    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String RSA_MODE =  "RSA/ECB/PKCS1Padding";
    private final int GETKEY_RETRY_COUNT = 3;
    private KeyStore keyStore;
    private String keyAlias;
    private KeyStore.PrivateKeyEntry privateKeyEntry;

    public static KeyStoreHelper getInstance(String keyAlias) {
        if (singletonInstance == null) {
            try {
                singletonInstance = new KeyStoreHelper(keyAlias);
            } catch (Exception ex) {
                System.out.println("Could not retrieve key store instance! " + ex.getMessage());
            }
        }
        return singletonInstance;
    }

    private KeyStoreHelper(String keyAlias) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        this.keyAlias = keyAlias;
        keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);
        try {
            privateKeyEntry = internalGetKey();
        } catch (UnrecoverableEntryException ex) {
//            deleteKey();
        }
    }

    public char[] getKey(Context context) {
        String passphrase = SharedPreferencesManager.getDbPassphrase();

        if (!keyExists()) {

            try {
                generateKey(context);
            } catch (Exception ex) {
                System.out.println("Could not generate key: " + ex.getMessage());
            }


            if (passphrase != null) {
                Log.w("OpenDatabase", "database passphrase could not be recovered");
                SharedPreferencesManager.removeDbPassphrase();
            }
        }

        if (passphrase == null) {
            try {
                passphrase = pickRandomPassphraseAndEncryptRSA();
            } catch (Exception ex) {
                System.out.println("Could not generate random passphrase and encrypt it with rsa! " + ex.getMessage());
            }
            SharedPreferencesManager.setDbPassphrase(passphrase);
        }

        byte[] decryptedPassphrase = null;
        try {
            decryptedPassphrase = rsaDecrypt(Base64.decode(passphrase, Base64.DEFAULT));
        } catch (Exception ex) {
            System.out.println("Could not decrypt passphrase! " + ex.getMessage());
        }

        char[] charPassphrase = new char[decryptedPassphrase.length];
        for (int i = 0; i < decryptedPassphrase.length; ++i) {
            charPassphrase[i] = (char) (decryptedPassphrase[i] & 0xFF);
        }

        return charPassphrase;
    }

    public boolean keyExists() {
        return privateKeyEntry != null;
    }

    private boolean deleteKey() throws KeyStoreException {
        if (keyStore.containsAlias(keyAlias)) {
            keyStore.deleteEntry(keyAlias);
            return true;
        }
        return false;
    }

    private KeyStore.PrivateKeyEntry internalGetKey() throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException {
        return getKey(0);
    }

    private void generateKey(Context context) throws KeyStoreException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, UnrecoverableEntryException {
        if (Build.VERSION.SDK_INT == 21) {
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
            privateKeyEntry = internalGetKey();
        } else {
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
            privateKeyEntry = internalGetKey();
        }
    }

    private String pickRandomPassphraseAndEncryptRSA() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        byte[] key = new byte[16];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        byte[] encryptedKey = rsaEncrypt(key);

        return Base64.encodeToString(encryptedKey, Base64.DEFAULT);
    }

    private KeyStore.PrivateKeyEntry getKey(int tryCounter) throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException {
        if (!keyStore.containsAlias(keyAlias)) return null;
        try {
            return (KeyStore.PrivateKeyEntry)keyStore.getEntry(keyAlias, null);
        } catch (UnrecoverableKeyException ex) {
            if (tryCounter < GETKEY_RETRY_COUNT) {
                return getKey(tryCounter + 1);
            } else {
                throw ex;
            }
        }
    }

    // from https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
    private byte[] rsaEncrypt(byte[] secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
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

    // from https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
    private byte[] rsaDecrypt(byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
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
}
