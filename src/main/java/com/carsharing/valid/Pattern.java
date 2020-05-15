package com.carsharing.valid;

import static java.util.regex.Pattern.compile;

public class Pattern {

    public static boolean checkPassword(String password) {
        return compile("^(?=.*[a-z])[a-zA-Z\\d]{6,25}$")
                .matcher(password)
                .matches();
    }

    public static boolean checkPhone(String phone) {
        return compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
                .matcher(phone)
                .matches();
    }

    public static boolean checkEmail(String email) {
        return compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$")
                .matcher(email)
                .matches();
    }

}
