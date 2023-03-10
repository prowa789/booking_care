package com.booking_care;

public class test {

    public static String createUserName(String username) {
        String output = "";

        String[] splitName = username.toLowerCase().split(" ");

        output = splitName[splitName.length - 1] + ".";

        for (int i = 0; i < splitName.length - 1; i++) {
            output += splitName[i].charAt(0);
        }

        return output;
    }

    public static void main(String[] args) {
        System.out.println(createUserName("Lê Tuấn Anh"));
    }

}

