package ru.gotoqa.util;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Scanner;

public class Utils {

    /**
     * Encode to Base64URL
     *
     * @param strForEncode - String for code
     * @return - Base64URL(String)
     */
    public String encodeBase64Url(String strForEncode) {
        byte[] bytes = Base64.encodeBase64URLSafe(strForEncode.getBytes());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Get day
     * 86400 sec per day
     *
     * @param days - required number of days before/after
     * @return Date 1606834210 - example GMT: Tuesday, 1 December 2020 г., 14:50:10
     */
    public static Date getFlexibleDay(int days) {
        return Date.from(Instant.now().plusSeconds(86400 * days));
    }

    /**
     * Generate Id
     */
    public static String nextId(int index) {
        return java.util.UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, index);
    }

    /**
     * Reading a plain text file
     *
     * @param filePath - Path to file
     */
    public static String getStringFromFile(String filePath) {
        String loadString = null;
        try {
            Scanner scanner = new Scanner(new File(filePath));
            loadString = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return loadString;
    }

}
