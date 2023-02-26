package com.booking_care.utils;


import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String dateToString(Date date){
        return null;
    }
    public static Date stringToDate(String dateStr){
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
            return format.parse(dateStr);
        }catch (Exception e) {
            System.out.println("lỗi parse date");
        }
        return null;
    }
    public static Date stringToDate2(String dateStr){
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(dateStr);
        }catch (Exception e) {
            System.out.println("lỗi parse date");
        }
        return null;
    }
    public static String encode(String key, String data) {
        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }
}
