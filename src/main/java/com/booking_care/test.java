package com.booking_care;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class test {

    //    public static String createRandomCode(int codeLength){
//        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
//        StringBuilder sb = new StringBuilder();
//        Random random = new SecureRandom();
//        for (int i = 0; i < codeLength; i++) {
//            char c = chars[random.nextInt(chars.length)];
//            sb.append(c);
//        }
//        String output = sb.toString();
////        System.out.println(output);
//        return output ;
//    }
//
//    public static void main(String[] args) {
////        System.out.println(createRandomCode(10));
//        System.out.println(new Date().getDate());
//    }
    public static void main(String[] args) {
        try {
            URL url = new URL("http://10.2.9.136:8080/api/v1.0/nb/unregister-sim");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "token");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String requestBody = "[{\"imsi\":\"452022000260396\"},{\"imsi\":\"4520220002603911212\"}]";
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(requestBody);
            out.flush();
            out.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Process the response data
                System.out.println(response.toString());
            } else {
                System.out.println("POST request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

