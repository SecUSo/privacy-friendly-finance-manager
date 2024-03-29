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
import android.security.keystore.KeyGenParameterSpec;
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
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

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

    private static final String TAG = KeyStoreHelper.class.getName();

    private static final String androidKeyStore = "AndroidKeyStore";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private final int KEY_RETRIEVAL_COUNTER = 3;
    private final KeyStore keyStore;
    private final String keyAlias;
    private KeyStore.PrivateKeyEntry privateKeyEntry;

    public static KeyStoreHelper getInstance(String keyAlias) throws KeyStoreHelperException {
        if (singletonInstance == null) {
            synchronized (KeyStoreHelper.class) {
                if (singletonInstance == null) {
                    try {
                        singletonInstance = new KeyStoreHelper(keyAlias);
                    } catch (KeyStoreException e) {
                        throw new KeyStoreHelperException("No provider supports a KeyStoreSpi implementation for the specified type", e);
                    } catch (CertificateException e) {
                        throw new KeyStoreHelperException("A certificate could not be loaded", e);
                    } catch (IOException e) {
                        throw new KeyStoreHelperException("I/O format problem", e);
                    } catch (NoSuchAlgorithmException e) {
                        throw new KeyStoreHelperException("Algorithm to check keystore integrity not found", e);
                    }
                }
            }
        }

        return singletonInstance;
    }

    /**
     * @param keyAlias Key alias
     * @throws KeyStoreException        - if no Provider supports a KeyStoreSpi implementation for the specified type
     * @throws CertificateException     – if any of the certificates in the keystore could not be loaded
     * @throws IOException              - if there is an I/O or format problem with the keystore data. If the error is due
     *                                  to an incorrect ProtectionParameter (e.g. wrong password) the cause of the IOException should
     *                                  be an UnrecoverableKeyException
     * @throws NoSuchAlgorithmException – if the algorithm used to check the integrity of the keystore cannot be found
     */
    private KeyStoreHelper(String keyAlias) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        this.keyAlias = keyAlias;
        keyStore = KeyStore.getInstance(androidKeyStore);
        keyStore.load(null);
        try {
            privateKeyEntry = getKey(0);
        } catch (UnrecoverableEntryException ex) {
//            deleteKey();
        }
    }

    public char[] getKey(Context context) {
        String passphrase = SharedPreferencesManager.get(context).getDbPassphrase();

        if (!keyExists()) {
            try {
                generateKey(context);
            } catch (KeyStoreException | InvalidAlgorithmParameterException
                    | NoSuchAlgorithmException | UnrecoverableEntryException | NoSuchProviderException ex) {
                Log.e(TAG, "Could not generate key: " + ex.getMessage(), ex);
            }

            if (passphrase != null) {
                Log.w(TAG, "database passphrase could not be recovered");
                SharedPreferencesManager.get(context).removeDbPassphrase();
            }
        }

        if (passphrase == null) {
            try {
                passphrase = pickRandomPassphraseAndEncryptRSA();
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException ex) {
                Log.e(TAG, "Could not pick random passphrase and encrypt it: " + ex.getMessage(), ex);
            }
            SharedPreferencesManager.get(context).setDbPassphrase(passphrase);
        }

        byte[] decryptedPassphrase = new byte[0];
        try {
            decryptedPassphrase = rsaDecrypt(Base64.decode(passphrase, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException ex) {
            Log.e(TAG, "Could not decrypt passphrase with rsa: " + ex.getMessage(), ex);
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

    /**
     * @return
     * @throws KeyStoreException - if the keystore has not yet been initialized or the entry cannot be removed
     */
    public boolean deleteKey() throws KeyStoreException {
        if (keyStore.containsAlias(keyAlias)) {
            keyStore.deleteEntry(keyAlias);
            privateKeyEntry = null;
            return true;
        }
        return false;
    }

    private void generateKey(Context context) throws InvalidAlgorithmParameterException, UnrecoverableEntryException, KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException {
        deleteKey();

        X500Principal subject = new X500Principal("CN=" + keyAlias);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, androidKeyStore);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
            KeyGenParameterSpec keyGenSpec = builder.setKeySize(4096)
                    .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .setRandomizedEncryptionRequired(false)
                    .setUserAuthenticationRequired(false)
                    .build();
            kpg.initialize(keyGenSpec);
        } else {
            KeyPairGeneratorSpec.Builder builder = new KeyPairGeneratorSpec.Builder(context);
            KeyPairGeneratorSpec keyPairGenSpec = builder.setAlias(keyAlias)
                    .setSerialNumber(BigInteger.ONE)
                    .setSubject(subject)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .setKeySize(4096)
                    .build();
            kpg.initialize(keyPairGenSpec);
        }

        kpg.generateKeyPair();

        privateKeyEntry = getKey(0);
    }

    private String pickRandomPassphraseAndEncryptRSA() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        byte[] key = new byte[16];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        byte[] encryptedKey = rsaEncrypt(key);

        return Base64.encodeToString(encryptedKey, Base64.DEFAULT);
    }

    private KeyStore.PrivateKeyEntry getKey(int tryCounter) throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException {
        if (!keyStore.containsAlias(keyAlias)) {
            return null;
        }

        try {
            return (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAlias, null);
        } catch (UnrecoverableKeyException ex) {
            if (tryCounter < KEY_RETRIEVAL_COUNTER) {
                return getKey(tryCounter + 1);
            } else {
                throw ex;
            }
        }
    }

    // Source: https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
    private byte[] rsaEncrypt(byte[] secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        Cipher inputCipher = Cipher.getInstance(RSA_MODE);
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
        cipherOutputStream.write(secret);
        cipherOutputStream.close();

        return outputStream.toByteArray();
    }

    // Source: https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
    private byte[] rsaDecrypt(byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        Cipher output = Cipher.getInstance(RSA_MODE);
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(encrypted), output);
        List<Byte> values = new LinkedList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte) nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i);
        }
        return bytes;
    }
}
