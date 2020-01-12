package com.carsharing.valid;

import java.util.regex.Matcher;

public class Pattern {

    public static boolean checkPassword(String testString) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("^(?=.*[a-z])[a-zA-Z\\d]{6,25}$");
        Matcher m = p.matcher(testString);
        return m.matches();
    }

    public static boolean checkTelNumber(String testString) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        Matcher m = p.matcher(testString);
        return m.matches();
    }

    public static boolean checkMail(String testString) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
        Matcher m = p.matcher(testString);
        return m.matches();
    }

}
