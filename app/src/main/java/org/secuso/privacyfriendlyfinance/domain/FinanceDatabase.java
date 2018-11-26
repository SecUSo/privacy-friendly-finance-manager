package org.secuso.privacyfriendlyfinance.domain;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import com.commonsware.cwac.saferoom.SafeHelperFactory;

import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.convert.DateTimeConverter;
import org.secuso.privacyfriendlyfinance.domain.convert.LocalDateConverter;
import org.secuso.privacyfriendlyfinance.domain.legacy.MigrationFromUnencrypted;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.helpers.SharedPreferencesManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

@Database(
    entities = {Account.class, Category.class, Transaction.class},
    exportSchema = false,
    version = 5
)
@TypeConverters({DateTimeConverter.class, LocalDateConverter.class})
public abstract class FinanceDatabase extends RoomDatabase {
    private static final String DB_NAME = "encryptedDB";
    private static InitDatabaseTask initTask;
    private static FinanceDatabase instance;

    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();
    public abstract AccountDao accountDao();

    public static FinanceDatabase getInstance() {
        return instance;
    }

    public static CommunicantAsyncTask<?, FinanceDatabase> connect(Context context, TaskListener listener) {
        if (initTask == null) {
            initTask = new InitDatabaseTask(context, DB_NAME);
            if (listener != null) initTask.addListener(listener);
            initTask.execute();
        }
        return initTask;
    }


    private static class InitDatabaseTask extends CommunicantAsyncTask<Void, FinanceDatabase> {
        private static final String AndroidKeyStore = "AndroidKeyStore";
        private static final String RSA_MODE =  "RSA/ECB/PKCS1Padding";
        private static final String KEY_ALIAS =  "financeDatabaseKey";

        private Context context;
        private String dbName;
        private KeyStore keyStore;


        public InitDatabaseTask(Context context, String dbName) {
            this.context = context;
            this.dbName = dbName;
        }

        // from https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
        private void initKeyStore() throws Exception {
            keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);
            // Generate the RSA key pairs
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                // Generate a key pair for encryption
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);
                KeyPairGeneratorSpec spec = new      KeyPairGeneratorSpec.Builder(context)
                        .setAlias(KEY_ALIAS)
                        .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                        .setSerialNumber(BigInteger.TEN)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA);
                kpg.initialize(spec);
                kpg.generateKeyPair();
            }
        }

        // from https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
        private byte[] rsaEncrypt(byte[] secret) throws Exception {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
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
        private byte[] rsaDecrypt(byte[] encrypted) throws Exception {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(KEY_ALIAS, null);
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

        protected String createPassphrase() throws Exception {
                byte[] key = new byte[16];
                SecureRandom secureRandom = new SecureRandom();
                secureRandom.nextBytes(key);
                byte[] encryptedKey = rsaEncrypt(key);
                return Base64.encodeToString(encryptedKey, Base64.DEFAULT);
        }

        private void deleteDatabaseFile() {
            File databaseDir = new File(context.getApplicationInfo().dataDir + "/databases");
            File[] files = databaseDir.listFiles();
            if(files != null) {
                for(File f: files) {
                    if(!f.isDirectory() && f.getName().startsWith(DB_NAME)) {
                        f.delete();
                    }
                }
            }
        }

        private boolean dbFileExists() {
            File databaseFile = new File(context.getApplicationInfo().dataDir + "/databases/" + DB_NAME);
            return databaseFile.exists() && databaseFile.isFile();
        }

        @Override
        protected FinanceDatabase doInBackground(Void... voids) {
            try {
                publishProgress(0.0);
                publishOperation("init key store");
                initKeyStore();
                publishProgress(.2);

                String passphrase = SharedPreferencesManager.getDbPassphrase();
                if (passphrase == null) {
                    deleteDatabaseFile();
                    publishOperation("create passphrase");
                    passphrase = createPassphrase();
                    SharedPreferencesManager.setDbPassphrase(passphrase);
                }

                publishProgress(.4);
                publishOperation("decrypt passphrase");
                byte[] decryptedPassphrase = rsaDecrypt(Base64.decode(passphrase, Base64.DEFAULT));

                char[] charPassphrase = new char[decryptedPassphrase.length];
                for (int i = 0; i < decryptedPassphrase.length; ++i) {
                    charPassphrase[i] = (char) (decryptedPassphrase[i] & 0xFF);
                }

                publishProgress(.6);
                if (dbFileExists()) {
                    publishOperation("open database");
                } else {
                    publishOperation("create and open database");
                }
                FinanceDatabase.instance = Room.databaseBuilder(context, FinanceDatabase.class, dbName)
                        .openHelperFactory(new SafeHelperFactory(charPassphrase))
                        .fallbackToDestructiveMigration()
                        .build();

                if (FinanceDatabase.instance.accountDao().getAll().size() == 0) {
                    Account defaultAccount = new Account("DefaultAccount", 0L);
                    defaultAccount.setId(0L);
                    FinanceDatabase.instance.accountDao().insert(defaultAccount);
                }

                if (MigrationFromUnencrypted.legacyDatabaseExists(context)) {
                    publishProgress(.8);
                    publishOperation("migrate database");
//                    MigrationFromUnencrypted.migrateTo(FinanceDatabase.instance, context);
//                    MigrationFromUnencrypted.deleteLegacyDatabase(context);
                }
                return FinanceDatabase.instance;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
