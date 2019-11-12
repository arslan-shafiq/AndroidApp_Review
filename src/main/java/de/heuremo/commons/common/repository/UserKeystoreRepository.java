package de.heuremo.commons.common.repository;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class UserKeystoreRepository {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "HeuremoKeyStore";

    private KeyStore keyStore;

    private byte[] encryption;
    private byte[] iv;

    public UserKeystoreRepository()
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
    }

    /**
     * We need to use API level 19 for this to work properly
     *
     * @param alias
     * @param encryptedData
     * @return
     * @throws UnrecoverableEntryException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws NoSuchProviderException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException public String decryptData(final String alias, final byte[] encryptedData)
     *                                            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
     *                                            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
     *                                            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
     *                                            <p>
     *                                            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
     *                                            final GCMParameterSpec spec = new GCMParameterSpec(128, iv);
     *                                            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec);
     *                                            <p>
     *                                            return new String(cipher.doFinal(encryptedData), "UTF-8");
     *                                            }
     */
    private SecretKey getSecretKey(final String alias)
            throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
    }

    public byte[] encryptText(final String alias, final String textToEncrypt)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

        iv = cipher.getIV();

        return (encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
    }

    public byte[] getEncryption() {
        return encryption;
    }

    public byte[] getIv() {
        return iv;
    }
}
