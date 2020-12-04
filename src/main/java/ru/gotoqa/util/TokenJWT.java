package ru.gotoqa.util;

import com.google.common.base.Splitter;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.jupiter.api.Assertions;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * Utilities for uses Digital Signature
 * (Public / Private PKCS8 format)
 * RSA (1024, 2048) java.security
 * Encoding PEM
 */
public class TokenJWT {

    /**
     * Load Public key from file (PKCS1 RSA Cryptography Standard)
     *
     * @param filename - path to key
     * @return - RSAPublicKey The interface to an RSA public key. java.security
     */
    public static RSAPublicKey readPublicKeyFromFile(String filename) {
        try (FileReader keyReader = new FileReader(filename);
             PemReader pemReader = new PemReader(keyReader)) {

            KeyFactory factory = KeyFactory.getInstance("RSA");
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
            return (RSAPublicKey) factory.generatePublic(pubKeySpec);
        } catch (Exception ex) {
            Assertions.assertNull(ex.getMessage(), "The key was not loaded");
            return null;
        }
    }

    /**
     * Load Private key from file (PKCS8 RSA Cryptography Standard)
     *
     * @param filename - path to a key
     * @return - RSAPrivateKey The interface to an RSA private key. java.security
     */
    public static RSAPrivateKey readPrivateKeyFromFile(String filename) throws Exception {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);

        byte[] keyBytes = new byte[(int) file.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    /**
     * KEYS generator
     * default - RSA/2048
     *
     * @return KeyPair - keys container
     */
    public static KeyPair generateSHA256KeyPair(String algorithm, Integer keySize) {
        KeyPairGenerator kpg = null;
        try {
            algorithm = algorithm == null ? "RSA" : algorithm;
            keySize = keySize == null ? 2048 : keySize;
            kpg = KeyPairGenerator.getInstance(algorithm);
            kpg.initialize(keySize);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(kpg).generateKeyPair();
    }

    public static void main(String[] args) throws Exception {
        saveKeyPairToFile("src/main/resources/examples");
    }

    /**
     * Record key to file - PKCS1 RSA Cryptography Standard
     */
    public static void saveKeyPairToFile(String filePath) throws Exception {
        KeyPair keyPair = generateSHA256KeyPair(null, null);

        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        String publicKeyContent = Base64.encodeBase64URLSafeString(publicKeyBytes);
        StringBuilder publicKeyFormatted = new StringBuilder(
                "-----BEGIN PUBLIC KEY-----" + System.lineSeparator());
        for (final String row :
                Splitter
                        .fixedLength(64)
                        .split(publicKeyContent)
        ) {
            publicKeyFormatted.append(row).append(System.lineSeparator());
        }
        publicKeyFormatted.append(
                "-----END PUBLIC KEY-----");
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath + "/public.key"));
        writer.write(publicKeyFormatted.toString());
        writer.close();

        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        String privateKeyContent = Base64.encodeBase64URLSafeString(privateKeyBytes);
        StringBuilder privateKeyFormatted = new StringBuilder(
                "-----BEGIN PRIVATE KEY-----" + System.lineSeparator());
        for (final String row :
                Splitter
                        .fixedLength(64)
                        .split(privateKeyContent)
        ) {
            privateKeyFormatted.append(row).append(System.lineSeparator());
        }
        privateKeyFormatted.append(
                "-----END PRIVATE KEY-----");
        BufferedWriter writer2 = new BufferedWriter(
                new FileWriter(filePath + "/private.key"));
        writer2.write(privateKeyFormatted.toString());
        writer2.close();
    }

}
