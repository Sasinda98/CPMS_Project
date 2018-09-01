package com.construction.app.cpms.userManagement;


//This class handles basic form validations.
public class FormValidator {

    private final static int MIN_EMAIL_LENGTH = 1;
    private final static int MIN_PASSWORD_LENGTH = 5;
    private final static int MIN_NAME_LENGTH = 5;
    private final static int MIN_PHONE_LENGTH = 10;


    public static boolean isPasswordEntryCorrect(String password){
        if(password.length() >= MIN_PASSWORD_LENGTH){
            return true;
        }
        return false;
    }

    public static boolean isNameValid(String name){
        if(name.length() >= MIN_NAME_LENGTH ){
            return true;
        }
        return false;
    }

    public static boolean isPhoneValid(String phoneNum){
        if(phoneNum.length() >= MIN_PHONE_LENGTH){
            return true;
        }
        return false;
    }

    public static boolean isEmailValid(String email){
        if(email.length() >= MIN_EMAIL_LENGTH){
            return true;
        }
        return false;
    }


}
